/*
 * DataSales.java
 *
 * Created on 2006/05/26, 9:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sosia.pos.data.account.DataPayment;
import com.geobeck.sosia.pos.hair.data.course.DataContract;
import com.geobeck.sosia.pos.hair.data.course.DataContractDigestion;
import com.geobeck.sosia.pos.master.customer.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.products.*;
import com.geobeck.sosia.pos.hair.data.product.*;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.hair.master.company.MstResponse;
import com.geobeck.sosia.pos.hair.master.product.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.sql.*;
import java.util.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 伝票ヘッダデータ
 *
 * @author katagiri
 */
public class DataSales extends ArrayList<DataSalesDetail> {
    //割引区分

    public static final int DISCOUNT_DIVISION_ALL = 0;
    public static final int DISCOUNT_DIVISION_TECHNIC = 1;
    public static final int DISCOUNT_DIVISION_PRODUCT = 2;
    //技術商品区分ＩＤ
    public static final int PRODUCT_DIVISION_DISCOUNT = 0;
    public static final int PRODUCT_DIVISION_TECHNIC = 1;
    //nhanvt start edit 20150128 Bug #35087
    public static final int PRODUCT_DIVISION_COURSE = 5;
    //nhanvt end edit 20150128 Bug #35087
    public static final int PRODUCT_DIVISION_ITEM = 2;
    //nhanvt start add 20150326 Bug #35729
    private MstAccountSetting accountSetting = null;
    //nhanvt end add 20150326 Bug #35729
    private Boolean applyAccountFilter = false;
    private Integer type = 0;
    /**
     * 店舗
     */
    protected MstShop shop = new MstShop();
    /**
     * 伝票No.
     */
    protected Integer slipNo = null;
    /**
     * 売上日
     */
    protected java.util.Date salesDate = null;
    /**
     * 顧客データ
     */
    protected MstCustomer customer = new MstCustomer();
    /**
     * 指名
     */
    protected boolean designated = false;
    /**
     * 主担当スタッフ
     */
    protected MstStaff staff = null;
    /**
     * 来店回数
     */
    protected Integer visitNum = null;
    /**
     * 割引データ
     */
    protected DataSalesDetail discount = new DataSalesDetail();
    /**
     * 支払データ
     */
    protected ArrayList<DataPayment> payments = new ArrayList<DataPayment>();
    /**
     * 一時保存用フラグ
     */
    protected boolean tempFlag = false;
    /**
     * 来店メモ
     */
    protected String visitedMemo = "";
    /**
     * 次回訪問日
     */
    protected java.util.Date nextVisitDate = null;
    /**
     * 開始予定日時
     */
    protected Timestamp reservationDatetime = null;

    /*
     * 開拓者コード
     */
    private Integer m_intPioneerCode;
    /*
     * モバイル予約フラグ
     */
    private Integer mobileFlag = null;
    /**
     * 来店時次回予約フラグ
     */
    private Integer nextFlag = null;
    /*
     * 事前予約フラグ
     */
    private Integer preorderFlag = null;
    // 新規伝票かどうか
    private boolean newAccount = false;
    //発行されたコース契約番号
    private Integer issuedContractNo = null;
    private boolean changePickup = false;

    public Integer getIssuedContractNo() {
        return issuedContractNo;
    }

    public void setIssuedContractNo(Integer issuedContractNo) {
        this.issuedContractNo = issuedContractNo;
    }
    //コース契約リスト
    List<DataSalesDetail> courseSalesList = new ArrayList<DataSalesDetail>();
    ;
        //コース消化リスト
        List<DataSalesDetail> courseConsumptionList = new ArrayList<DataSalesDetail>();
    ;
        //契約コース格納マップ
        Map<String, DataContract> dataContractMap = new HashMap<String, DataContract>();
    // Start add 20130517 nakhoa
    protected ArrayList<String> cardName = new ArrayList<String>();
    // End add 20130517 nakhoa
    // Start add 20131031 lvut
    protected MstResponse Response1 = null;
    protected MstResponse Response2 = null;
    
    protected  Integer ProposalID   = null;

    public Integer getProposalID() {
        return ProposalID;
    }

    public void setProposalID(Integer ProposalID) {
        this.ProposalID = ProposalID;
    }

    public MstResponse getResponse1() {
        return Response1;
    }

    public void setResponse1(MstResponse Response1) {
        this.Response1 = Response1;
    }

    public MstResponse getResponse2() {
        return Response2;
    }

    public void setResponse2(MstResponse Response2) {
        this.Response2 = Response2;
    }

    public boolean isChangePickup() {
        return changePickup;
    }

    public void setChangePickup(boolean changePickup) {
        this.changePickup = changePickup;
    }

    // End add 20131031 lvut
    /**
     * Creates a new instance of DataSales
     */
    public DataSales() {
    }

    public DataSales(Integer type) {
        this.setType(type);
        discount.setProductDivision(0);
    }
    
    //nhanvt start add 20150326 Bug #35729
    public MstAccountSetting getAccountSetting() {
        return accountSetting;
    }

    public void setAccountSetting(MstAccountSetting accountSetting) {
        this.accountSetting = accountSetting;
    }
    //nhanvt end add 20150326 Bug #35729

    public Boolean isApplyAccountFilter() {
        return applyAccountFilter;
    }

    public void setApplyAccountFilter(Boolean accountFilter) {
        this.applyAccountFilter = applyAccountFilter;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setVisitedMemo(String memo) {
        this.visitedMemo = memo;
    }

    public String getVisitedMemo() {
        return this.visitedMemo;
    }

    public void setNextVisitDate(java.util.Date d) {
        this.nextVisitDate = d;
    }

    public java.util.Date getNextVisitDate() {
        return this.nextVisitDate;
    }

    public void setReservationDatetime(Timestamp t) {
        this.reservationDatetime = t;
    }

    public Timestamp getReservationDatetime() {
        return this.reservationDatetime;
    }

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
     * 伝票No.を取得する。
     *
     * @return 伝票No.
     */
    public Integer getSlipNo() {
        return slipNo;
    }

    /**
     * 伝票No.をセットする。
     *
     * @param slipNo 伝票No.
     */
    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * 売上日を取得する。
     *
     * @return 売上日
     */
    public java.util.Date getSalesDate() {
        return salesDate;
    }

    /**
     * 売上日をセットする。
     *
     * @param salesDate 売上日
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * 顧客データを取得する。
     *
     * @return 顧客データ
     */
    public MstCustomer getCustomer() {
        return customer;
    }

    /**
     * 顧客データをセットする。
     *
     * @param customer 顧客データ
     */
    public void setCustomer(MstCustomer customer) {
        this.customer = customer;
    }

    /**
     * 指名を取得する。
     *
     * @return 指名
     */
    public boolean getDesignated() {
        return designated;
    }

    /**
     * 指名をセットする。
     *
     * @param designated 指名
     */
    public void setDesignated(boolean designated) {
        this.designated = designated;
    }

    /**
     * 主担当スタッフを取得する。
     *
     * @return 主担当スタッフ
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * 主担当スタッフをセットする。
     *
     * @param MstStaff 主担当スタッフ
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * 来店回数を取得する。
     *
     * @return 来店回数
     */
    public Integer getVisitNum() {
        return visitNum;
    }

    /**
     * 来店回数をセットする。
     *
     * @param visitNum 来店回数
     */
    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    /**
     * 一時保存用フラグをセットする。
     *
     * @param　一時保存用フラグ
     */
    public boolean getTempFlag() {
        return tempFlag;
    }

    /**
     * 一時保存用フラグを取得する。
     *
     * @param　一時保存用フラグ
     * @return 一時保存用フラグ
     */
    public void setTempFlag(boolean tempFlag) {
        this.tempFlag = tempFlag;
    }

    /**
     * 売上明細を読み込む。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean loadDetail(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery("");

        return true;
    }

    /**
     * 売上明細を取得するＳＱＬ文を取得する。
     *
     * @return 売上明細を取得するＳＱＬ文
     */
    public String getLoadDetailSQL() {
        return "";
    }

    /**
     * 割引データを取得する。
     *
     * @return 割引データ
     */
    public DataSalesDetail getDiscount() {
        return discount;
    }

    /**
     * 割引データをセットする。
     *
     * @param discounts 割引データ
     */
    public void setDiscount(DataSalesDetail discount) {
        this.discount = discount;
    }

    /**
     * 支払データを取得する。
     *
     * @return 支払データ
     */
    public ArrayList<DataPayment> getPayments() {
        return payments;
    }

    /**
     * 支払データをセットする。
     *
     * @param payments 支払データ
     */
    public void setPayments(ArrayList<DataPayment> payments) {
        this.payments = payments;
    }
    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
    public ArrayList<DataSalesMainstaff> salesMainStaffs = new ArrayList<DataSalesMainstaff>();
    public ArrayList<DataSalesMainstaff> getSalesMainstaffs() {
        return salesMainStaffs;
    }

    /**
     * 支払データをセットする。
     *
     * @param DataSalesMainstaff 支払データ
     */
    public void setSalesMainstaffs(ArrayList<DataSalesMainstaff> salesMainStaffs) {
        this.salesMainStaffs = salesMainStaffs;
    }
    // IVS_Thanh end add 2014/07/14 Mashu_お会計表示

    /**
     * 支払データを取得する。
     *
     * @param paymentNo 支払No.
     * @return 支払データ
     */
    public DataPayment getPayment(int index) {
        return payments.get(index);
    }

    /**
     * 支払データを追加する。
     *
     * @param paymentNo 支払No.
     * @param payment 支払データ
     */
    public void addPayment(DataPayment payment) {
        payment.setShop(this.getShop());
        payment.setSlipNo(this.slipNo);
        payment.setPaymentNo(payments.size());
        payments.add(payment);
    }

    /**
     * 支払データの数を取得する。
     *
     * @return 支払データの数
     */
    public int paymentSize() {
        return this.payments.size();
    }

    /**
     * 金額の合計を取得する。
     *
     * @return 金額の合計
     */
    public Long getValueTotal() {
        Long total = 0l;

        for (DataSalesDetail dsd : this) {
            if (dsd.getProductDivision() == 6) {
                //コース消化の場合
//                            BigDecimal v = new BigDecimal(dsd.getValueForConsumption());
//                            v = v.setScale(0, RoundingMode.UP);
//                            total += v.longValue();
            } else {
                //コース消化以外の場合
                total += dsd.getValue();
            }
        }

        return total;
    }

    /**
     * 合計を取得する。
     *
     * @return 合計
     */
    public Long getSalesTotal() {
        Long total = 0l;

        for (DataSalesDetail dsd : this) {
            total += dsd.getTotal();
        }

        return total;
    }

    /**
     * 合計を取得する。
     *
     * @return 合計
     */
    public Long getSalesTotal(Integer productDivision) {
        Long total = 0l;

        for (DataSalesDetail dsd : this) {
            if (dsd.getProductDivision() == productDivision) {
                total += dsd.getTotal();
            }
        }

        return total;
    }

    /**
     * 合計を取得する。
     *
     * @return 合計
     */
    public Long getTechItemSalesTotal() {
        Long total = 0l;

        for (DataSalesDetail dsd : this) {
            //nhanvt start edit 20150128 Bug #35087
            if (dsd.getProductDivision() == PRODUCT_DIVISION_TECHNIC || dsd.getProductDivision() == PRODUCT_DIVISION_ITEM || dsd.getProductDivision() == PRODUCT_DIVISION_COURSE) {
                total += dsd.getTotal();
            }
            //nhanvt end edit 20150128 Bug #35087
        }

        return total;
    }

    public Long getDetailDiscountTotal() {
        Long total = 0l;

        for (DataSalesDetail dsd : this) {
            if (dsd.getDiscountValue() != null) {
                total += dsd.getDiscountValue();
            }
        }

        return total;
    }

    public long getAllDiscount() {
        Integer discountDivision = this.getDiscount().getDiscountDivision();

        if (discountDivision == null) {
            //割引種別が未設定の場合、全体割引はなし
            return new Long(0);
        }

        Long salesTotal = null;
        if (discountDivision == DISCOUNT_DIVISION_ALL) {
            //全体割引の場合、技術 + 商品の合計を取得
            salesTotal = this.getTechItemSalesTotal();
        } else {
            //技術(商品)割引の場合、技術(商品)のみの合計を取得
            salesTotal = this.getSalesTotal(discountDivision);
        }

        this.getDiscount().setProductValue(salesTotal);

        this.getDiscount().setProductNum(1);
        return this.getDiscount().getDiscountValue();
    }

    /**
     * 割引金額の合計を取得する。
     *
     * @return 割引金額の合計
     */
    public Long getDiscountTotal() {
        Long total = 0l;

        total += this.getDetailDiscountTotal();

        total += discount.getDiscountValue();

        return total;
    }

    /**
     * 消費税の合計を取得する。
     *
     * @return 消費税の合計
     */
    public Long getTaxTotal(Double taxRate, Integer rounding) {
        Long total = 0l;

        for (DataSalesDetail dsd : this) {
            total += dsd.getTax(taxRate, rounding);
        }

        return total;
    }
    // Start add 20130517 nakhoa

    public ArrayList<String> getCardName() {
        return this.cardName;
    }

    public void addCardName(String cardName) {
        this.cardName.add(cardName);
    }

    public String getCardName(int index) {
        return cardName.get(index);
    }
    // End add 20130517 nakhoa

    /**
     * ResultSetWrapperのデータをセットする。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException 例外
     */
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.setSlipNo(rs.getInt("slip_no"));
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSalesDate(rs.getDate("sales_date"));

        MstCustomer mc = new MstCustomer();
        mc.setCustomerID(rs.getInt("customer_id"));
        mc.setCustomerNo(rs.getString("customer_no"));
        mc.setCustomerName(0, rs.getString("customer_name1"));
        mc.setCustomerName(1, rs.getString("customer_name2"));
        MstSosiaCustomer msc = new MstSosiaCustomer();
        msc.setSosiaID(rs.getInt("sosia_id"));
        mc.setSosiaCustomer(msc);
        this.setCustomer(mc);
//		this.setDesignated( rs.getBoolean( "designated_flag" ) );
        this.setDesignated(rs.getBoolean("charge_staff_designated_flag"));

        MstStaff ms = new MstStaff();
        String tmp = rs.getString("charge_staff_id");
        ms.setStaffID(rs.wasNull() ? null : Integer.parseInt(tmp));

        tmp = rs.getString("visited_memo");
        this.setVisitedMemo(rs.wasNull() ? "" : tmp);

        ms.setStaffName(0, rs.getString("charge_staff_name1"));
        ms.setStaffName(1, rs.getString("charge_staff_name2"));
        this.setStaff(ms);
        if (type == 3) {
            this.setVisitNum(rs.getInt("visit_num"));
        }
        this.setNextVisitDate(rs.getDate("next_visit_date"));
    }

    /**
     * 新しい伝票No.をセットする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException 例外
     */
    public void setNewSlipNo(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getNewSlipNoSQL());

        if (rs.next()) {
            this.setSlipNo(rs.getInt("new_slip_no"));
        }
        rs.close();
    }

    /**
     * 全てのデータを登録する。
     *
     * @param con ConnectionWrapper
     * @param paymentNo 支払No.
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean registAll(ConnectionWrapper con, int paymentNo) throws SQLException {
        if (!this.deleteSlipChildren(con)) {
            return false;
        }
        //IVS_LVTu start add 2015/12/01 New request #44111
        //update slipNo exists
        if ( this.getProposalID() != null ) {
            if (!this.deleteDataProposal(con)) {
                return false;
            }
        }
        //IVS_LVTu end add 2015/12/01 New request #44111

        if (!this.regist(con)) {
            return false;
        }
        if (!this.updateDataProposal(con)) {
            return false;
        }

        //売上明細テーブル
        for (DataSalesDetail dsd : this) {
            dsd.setShop(this.getShop());
            dsd.setSlipNo(this.getSlipNo());
            //nhanvt start add 20150326 Bug #35729
            dsd.setAccountSetting(this.getAccountSetting());
            //nhanvt end add 20150326 Bug #35729
            if (dsd.getSlipDetailNo() == null) {
                dsd.setNewSlipDetailNo(con);
            }

            //コース消化の場合はコース消化リストに明細を追加しておく
            if (dsd.getProductDivision() == 6) {
                this.courseConsumptionList.add(dsd);

                //-- Deleted 2012/12/15 data_sales_detailに登録するように変更-----------------
                //コース消化の場合はdata_sales_detailのレコードは不要
                //continue;
                //-- Deleted 2012/12/15 ------------------------------------------------------

                //-- Inserted 2012/12/15 数量に「1」をセット----------------------------------
                dsd.setProductNum(1);
                //-- Inserted 2012/12/15 -----------------------------------------------------

            } else if (dsd.getProductDivision() == 5) {
                //コース契約
                dsd.setProductNum(1);
            }

            if (!dsd.regist(con)) {
                return false;
            }
            // 売上按分を登録
            for (DataSalesProportionally dsp : dsd) {
                dsp.setDataSalesDetail(dsd);
                if (!dsp.regist(con)) {
                    return false;
                }
            }
            //2016/09/05 GB MOD #54426 Start
            // 按分情報（商品シェア）が存在する場合、更新
            if (dsd.getProductDivision() == PRODUCT_DIVISION_ITEM) {
                if (!updateDetailProportionally(con, dsd)) {
                    return false;
                }                
            }
            //2016/09/05 GB MOD #54426 End
                      
            //コース販売の場合はコース契約リストに明細を追加しておく
            if (dsd.getProductDivision() == 5) {
                this.courseSalesList.add(dsd);
            }
            if (this.isChangePickup()) {
                //IVS NNTUAN START 20131129
                if (SystemInfo.getCurrentShop().getCourseFlag() != null
                        && SystemInfo.getCurrentShop().getCourseFlag().intValue() == 1) {
                    if (dsd != null && dsd.getProductDivision() != null) {
                        if (dsd.getProductDivision() == 2) {
                            // vtbphuong start change 20140620 Bug #25636
//                            if (SystemInfo.getNSystem() != 2) {
//                                dsd.setPickUp(true);
//                            }
//                            MstDataPickupProduct pp = new MstDataPickupProduct();
//                            pp.setShop(this.getShop());
//                            pp.setSlipNo(dsd.getSlipNo());
//                            pp.setSlipDetailNo(dsd.getSlipDetailNo());
//                            if (dsd.getPickUp()) {
//                                pp.setStatus(1);
//                                pp.setProductNum(0);
//                            } else {
//                                pp.setStatus(0);
//                                pp.setProductNum(dsd.getProductNum());
//                            }
//                            pp.setStaff(this.getStaff());
//                            pp.setOperationDate(this.getSalesDate());
//                            if (!pp.registSales(con)) {
//                                return false;
//                            }
                            // vtbphuong end change 20140620 Bug #25636
                            if (SystemInfo.getNSystem() == 2) {
                                MstDataPickupProduct pp = new MstDataPickupProduct();
                                pp.setShop(this.getShop());
                                pp.setSlipNo(dsd.getSlipNo());
                                pp.setSlipDetailNo(dsd.getSlipDetailNo());
                                if (dsd.getPickUp()) {
                                    pp.setStatus(1);
                                    pp.setProductNum(0);
                                } else {
                                    pp.setStatus(0);
                                    pp.setProductNum(dsd.getProductNum());
                                }
                                pp.setStaff(this.getStaff());
                                pp.setOperationDate(this.getSalesDate());
                                if (!pp.registSales(con)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
            //IVS NNTUAN END 20131129
        }

        //割引
        if (discount.getProduct() != null
                && discount.getProduct().getProductID() != null
                && discount.getDiscountValue() != 0) {
            discount.setShop(this.getShop());
            discount.setSlipNo(this.getSlipNo());
            discount.setNewSlipDetailNo(con);
            if (!discount.regist(con)) {
                return false;
            }
        }

        DataPayment dp = this.getPayments().get(paymentNo);

        dp.setShop(this.getShop());
        dp.setSlipNo(this.getSlipNo());
        dp.setTempFlag(this.getTempFlag());
        if (!dp.registAll(con)) {
            return false;
        }
        
        // Thanh start add 2014/07/10 Mashu_お会計表示
        for (DataSalesMainstaff dsm : this.getSalesMainstaffs()) {
            dsm.setSlipNo(this.getSlipNo());
            dsm.setShop(this.getShop());
            if (!dsm.regist(con)) {
                return false;
            }
        }
        // Thanh end add 2014/07/10 Mashu_お会計表示

        // 開拓者
        if (m_intPioneerCode != null) {
            registerPioneer(con);
        }

        // 初回来店日
        if (customer.getFirstVisitDate() == null) {
            StringBuilder sql = new StringBuilder(1000);
            sql.append(" update mst_customer");
            sql.append(" set");
            sql.append("     first_visit_date = ");
            sql.append("     coalesce((");
            sql.append("         select");
            sql.append("             min(sales_date)");
            sql.append("         from");
            sql.append("             view_data_sales_valid");
            sql.append("         where");
            sql.append("                 sales_date is not null");
            sql.append("             and customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
            sql.append("     ), " + SQLUtil.convertForSQLDateOnly(this.getSalesDate()) + ")");
            sql.append(" where");
            sql.append("     customer_id = " + SQLUtil.convertForSQL(customer.getCustomerID()));
            con.executeUpdate(sql.toString());
        }

        return true;
    }

    /**
     * コース契約履歴を登録する
     */
    public boolean registCourseContract(ConnectionWrapper con) throws SQLException {
        System.out.println("コース契約履歴の登録・更新処理");
        //コース契約リストが空でない場合
        if (!this.courseSalesList.isEmpty()) {
            //コースを購入した場合はコース契約履歴にも登録する
            System.out.println("コースを購入しているためコース契約履歴に登録を行なう");
            Integer contractNo = null;
//                Map<Integer, MstCourse> courseMap = new HashMap<Integer, MstCourse>();
            for (DataSalesDetail detail : this.courseSalesList) {
                System.out.println("コース契約履歴に登録するコース[" + detail.getProduct().getProductID() + "], [" + detail.getProductName() + "]");
                DataContract contract = null;

                if (newAccount) {
                    //伝票番号未発行
                    contract = new DataContract(detail);
                } else {
                    //伝票番号発行済み
                    if (detail.getDataContract() == null) {
                        //お会計の編集で新たににコース追加した
                        contract = new DataContract(detail);
                    } else {
                        //お会計の編集
                        contract = detail.getDataContract();
                         // vtbphuong start change 20140710 Request #26770
                        //contract.setProductValue(detail.getProductValue() - detail.getDiscountValue());
                        if(SystemInfo.getAccountSetting().getDiscountType() == 0 ){
                            contract.setProductValue (  detail.getProductValue() - detail.getDiscountValue() );
                        }else{
                            Double value = new Double(Math.floor(detail.getDiscountValue() + (detail.getDiscountValue() * detail.getTaxRate())));
                            contract.setProductValue ( detail.getProductValue() - value.longValue() ) ;
                        }
                         // vtbphuong end change 20140710 Request #26770
                    }
                }

                //新規伝票もしくは新たに（このお会計内で新たに）コース契約した場合は契約番号を発行する
                if (newAccount || issuedContractNo == null) {
                    if (contractNo == null) {
                        //最初の1回だけ契約NOを取得する
                        //以降は取得した契約Noを使いまわす
                        contractNo = contract.loadMaxContractNo(con, this.getShop().getShopID()) + 1;
                        System.out.println("最初の一回目だけ契約No取得");
                        //契約Noをセット
                        contract.setContractNo(contractNo);
                    }
                }

                if (contractNo == null) {
                    contractNo = contract.getContractNo();
                    //IVS_LVTu start add 2016/02/16 Bug #48426
                    if (contractNo == null) {
                        //最初の1回だけ契約NOを取得する
                        //以降は取得した契約Noを使いまわす
                        contractNo = contract.loadMaxContractNo(con, this.getShop().getShopID()) + 1;
                        System.out.println("最初の一回目だけ契約No取得");
                        //契約Noをセット
                        contract.setContractNo(contractNo);
                    }
                    //IVS_LVTu end add 2016/02/16 Bug #48426
                }

                if (contract.getContractNo() == null) {
                    //契約番号は発行済みの状態で新規のコースを追加契約した場合は契約番号をセット
                    contract.setContractNo(contractNo);
                }
                System.out.println("契約No[" + contract.getContractNo() + "]");

                //新規伝票もしくは新たにコース契約した場合はコース契約詳細番号を発行する
                if (newAccount || detail.getDataContract() == null) {
                    //契約詳細Noを取得
                    Integer contractDetailNo = contract.loadMaxContractDetailNo(con, this.getShop().getShopID(), contractNo) + 1;
                    //契約詳細Noをセット
                    contract.setContractDetailNo(contractDetailNo);
                }
                System.out.println("契約詳細No[" + contract.getContractDetailNo() + "]");
                // Start add 20131202 lvut
                int limitTime = 0;
                int securityTimeLimit = 0;
                if (detail.getCourse() != null) {
                    if (detail.getCourse().getPraiseTimeLimit() != null) {
                        limitTime = detail.getCourse().getPraiseTimeLimit().intValue();
                    }
                    if (detail.getCourse().getSecurityTimeLimit() != null) {
                        securityTimeLimit = detail.getCourse().getSecurityTimeLimit().intValue();
                    }
                }
                contract.setPraiseTimeLimit(limitTime);
                contract.setCourseSecurityTimeLimit(securityTimeLimit);
                // End add 20131202 lvut
                //契約するコース取得
//                    MstCourse course = null;
//                    if(courseMap.containsKey(contract.getProduct().getProductID()))
//                    {
//                        course = courseMap.get(contract.getProduct().getProductID());
//                    }
//                    else
//                    {
//                        System.out.println("契約するコース情報取得");
//                        course = contract.loadCourse(con, contract.getProduct().getProductID());
//                        courseMap.put(contract.getProduct().getProductID(), course);
//                    }
                //消化回数をセット
                contract.setProductNum(detail.getCourse().getNum());
                System.out.println("消化回数をセット[" + contract.getProductNum() + "]");

                // IVS SANG START INSERT 20131105 担当者
                contract.setStaff(detail.getStaff());
                // IVS SANG END INSERT 20131105 担当者
                //nhanvt start add 20150130 Bug #35115
                if(contract.getContractNo() == null && !newAccount){
                    contract.setContractNo(issuedContractNo);
                }
                //nhanvt end add 20150130 Bug #35115
                //コース契約履歴登録
                if (!contract.regist(con)) {
                    return false;
                }

                if (detail.getTmpContractNo() != null) {
                    //仮契約番号がある場合はMapに格納する
                    dataContractMap.put(detail.getTmpContractNo(), contract);
                }

                System.out.println("コース契約履歴登録");
            }
            this.courseSalesList = new ArrayList<DataSalesDetail>();
//                courseMap = null;
        }
        return true;
    }

    /**
     * コース消化履歴を登録する
     */
    public boolean registConsumptionCourse(ConnectionWrapper con) throws SQLException {
        //コース消化リストが空でない場合
        DataSalesDetail dt = null;
        if (!this.courseConsumptionList.isEmpty()) {
            //コースを消化した場合はコース消化履歴にも登録する
            System.out.println("コースを消化するためコース消化履歴に登録を行なう");
            for (DataSalesDetail detail : this.courseConsumptionList) {
                DataContractDigestion dcd = new DataContractDigestion(detail);
                
//                    // 契約元店舗ID
//                    dcd.setContractShopId(detail.getConsumptionCourse().getContractShopId());

                if (detail.getTmpContractNo() != null) {
                    //仮契約番号がある場合（購入と当時に消化する場合）は仮契約番号からコース契約データを取得しコース契約番号・コース契約詳細番号をセットする
                    DataContract dataContract = dataContractMap.get(detail.getTmpContractNo());
                    dcd.setContractNo(dataContract.getContractNo());
                    dcd.setContractDetailNo(dataContract.getContractDetailNo());

                }

                System.out.println("消化するコース情報");
                System.out.println("店舗ID[" + detail.getShop().getShopID() + "]");
                System.out.println("伝票番号[" + detail.slipNo + "]");
                System.out.println("契約No[" + detail.consumptionCourse.getContractNo() + "]");
                System.out.println("契約詳細No[" + detail.getConsumptionCourse().getContractDetailNo() + "]");

                //Luc start add 20140708 Request #26647 3.消化処理時に消化した契約の情報を上記カラムに登録する
                
                dt = (DataSalesDetail)detail.clone();
                dt.setContractShopId(detail.consumptionCourse.getContractShopId());
                dt.setContractNo(detail.consumptionCourse.getContractNo());
                dt.setContractDetailNo(detail.getConsumptionCourse().getContractDetailNo());
                if (!dt.regist(con)) {
                    return false;
                }
                //Luc end add 20140708 Request #26647 3.消化処理時に消化した契約の情報を上記カラムに登録する
                if (!dcd.regist(con)) {
                    return false;
                }

                System.out.println("コース消化履歴登録");
            }
        }
        this.courseConsumptionList = new ArrayList<DataSalesDetail>();
        this.dataContractMap = new HashMap<String, DataContract>();
        return true;
    }

    /**
     * 伝票ヘッダを登録する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean regist(ConnectionWrapper con) throws SQLException {
        String sql = "";

        if (isExists(con)) {
            sql = this.getUpdateSQL();
        } else {
            sql = this.getInsertSQL();
        }

        
        if (con.executeUpdate(sql) == 1) {
            System.out.println("●DataSales 更新sql:\n" + sql);
            return true;
            
        } else {
            System.out.println("●DataSales 更新失敗sql:\n" + sql);
            return false;
        }
    }

    //Thanh start add 2013/03/29
    /**
     * 伝票ヘッダを登録する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean registDataSale(ConnectionWrapper con) throws SQLException {
        String sql = "";
        sql = this.getInsertSQL();


        if (con.executeUpdate(sql) == 1) {
            System.out.println("●DataSales 更新sql:\n" + sql);
            return true;
        } else {
            System.out.println("●DataSales 更新失敗sql:\n" + sql);
            return false;
        }
    }
    //Thanh end add 2013/03/29

    /**
     * 伝票ヘッダの存在チェックを行う。
     *
     * @param con ConnectionWrapper
     * @return true - 存在する
     * @throws java.sql.SQLException 例外
     */
    public boolean isExists(ConnectionWrapper con) throws SQLException {
        if (this.getSlipNo() == null || this.getSlipNo() < 1) {
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
     * 伝票データを全て読み込む。
     *
     * @param con ConnectionWrapper
     * @param paymentNo 支払No.
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean loadAll(ConnectionWrapper con) throws SQLException {
        this.clear();
        discount = new DataSalesDetail();
        payments.clear();

        ResultSetWrapper rs = null;
        if ((SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev")) && SystemInfo.getSetteing().isUsePrepaid()) {
            rs = con.executeQuery(this.getSelectAllSQL2());
        } else {
            rs = con.executeQuery(this.getSelectAllSQL());
        }

        if (rs.next()) {
            this.setData(rs);
            this.getShop().setShopName(rs.getString("shop_name"));
            this.getShop().setOpenHour(rs.getInt("open_hour"));
            this.getShop().setOpenMinute(rs.getInt("open_minute"));
            this.getShop().setCloseHour(rs.getInt("close_hour"));
            this.getShop().setCloseMinute(rs.getInt("close_minute"));
            this.getShop().setDisplayProportionally(rs.getInt("display_proportionally"));
            if (this.getStaff() != null) {
                this.getStaff().setStaffNo(rs.getString("charge_staff_no"));
                this.getStaff().setStaffName(0, rs.getString("charge_staff_name1"));
                this.getStaff().setStaffName(1, rs.getString("charge_staff_name2"));
            }

            rs.beforeFirst();

            boolean hasCourseContractData = false;
            boolean hasConsumptionCourseData = false;
            while (rs.next()) {
                DataSalesDetail dsd = new DataSalesDetail();
                dsd.setAccountSetting(this.getAccountSetting());
                dsd.setData(rs);
                
                //nhanvt start
                
                //dsd.setAccountSetting(this.getAccountSetting());
                //nhanvt end
                if (dsd.getSlipDetailNo() == null || dsd.getSlipDetailNo() == 0) {
                    //伝票詳細番号がない場合はスキップ（コース消化はdata_sales_detailにレコードはない）
                    continue;
                }
                // 按分データを取得する
                loadSlipProportionally(con, dsd);
                if (dsd.getProductDivision() == 5 || dsd.getProductDivision() == 7) {
                    //コース契約の場合
                    Course course = new Course();
                    course.loadCourse(con, this.getShop().getShopID(), dsd.getProduct().getProductClass().getProductClassID(), dsd.getProduct().getProductID());
                    CourseClass courseClass = new CourseClass();
                    courseClass.setCourseClassId(dsd.getProduct().getProductClass().getProductClassID());
                    courseClass.setCourseClassName(dsd.getProduct().getProductClass().getProductClassName());
                    // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                    courseClass.setShopCategoryID(dsd.getProduct().getProductClass().getShopCategoryID());
                    // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
                    // vtbphuong start add 20150518
                    courseClass.setDisplaySeq(rs.getInt("class_display_seq"));
                    course.setCourseClass(courseClass);
                    // vtbphuong end add 20150518

                    Long productValue = dsd.getProductValue();
                    // IVS SANG START INSERT 20131010
                    if (dsd.getProduct() != null) {
                        if (course.getCourseName() == null || course.getCourseName().equals("")) {
                            course.setCourseName(dsd.getProduct().getProductName());
                        }
                        if (course.getCourseId() == null) {
                            course.setCourseId(dsd.getProduct().getProductID());
                        }
                        if (course.getOperationTime() == null) {
                            course.setOperationTime(dsd.getProduct().getOperationTime());
                        }
                    }
                    // IVS SANG END INSERT 20131010
                    dsd.setCourse(dsd.getProductDivision(), course);
                    dsd.setProductValue(productValue);

                    hasCourseContractData = true;
                } else if (dsd.getProductDivision() == 1) {
                    //dsd.setData(rs);に組み込みたいが既存のソースになるべく手を加えたくないのでここでoperation_timeをセット
                    dsd.getProduct().setOperationTime(rs.getInt("operation_time"));
                } else if (dsd.getProductDivision() == 8) {
                    Course course = new Course();
                    // IVS Hoa Start Edit 20150619
                    // [gb]解約伝票でコース名が表示されない
                    // マスタにコース削除した場合または店舗使用技術・商品マスタが存在しない場合
                    course.loadCourse(con, this.getShop().getShopID(), dsd.getProduct().getProductClass().getProductClassID(), dsd.getProduct().getProductID(), dsd.getSlipNo(),dsd.getSlipDetailNo());
                    // IVS Hoa End Edit 20150619
                    CourseClass courseClass = new CourseClass();
                    courseClass.setCourseClassId(dsd.getProduct().getProductClass().getProductClassID());
                    courseClass.setCourseClassName(dsd.getProduct().getProductClass().getProductClassName());
                    course.setCourseClass(courseClass);
                    Long productValue = dsd.getProductValue();
                    course.setCourseClass(courseClass);
                    dsd.setCourse(dsd.getProductDivision(), course);
                    dsd.setProductValue(productValue);

                } else if (dsd.getProductDivision() == 9) {
                    Course course = new Course();
                    CourseClass courseClass = new CourseClass();
                    courseClass.setCourseClassName("手数料");
                    Long productValue = dsd.getProductValue();
                    course.setCourseName("変更手数料");
                    course.setCourseClass(courseClass);
                    dsd.setCourse(dsd.getProductDivision(), course);
                    dsd.setProductValue(productValue);
                }
                this.add(dsd);
            }

            if (hasCourseContractData) {
                //コース契約情報を取得
                List<DataContract> dataContractList = DataContract.getDataContractList(con, this.getShop().getShopID(), this.getSlipNo());
                int no = 0;
                for (DataSalesDetail dsd : this) {
                    if (dsd.getProductDivision() == 5 || dsd.getProductDivision() == 7) {
                        DataContract dataContract = dataContractList.get(no);
                        dsd.setDataContract(dataContract);
                        dsd.getCourse().setNum(dataContract.getProductNum());

                        dsd.setEditable(!ConsumptionCourse.existConsumption(con, this.getShop().getShopID(), dataContract.getContractNo(), dataContract.getContractDetailNo()));
                        this.issuedContractNo = dataContract.getContractNo();
                        no++;
                    }
                }
            }

            //コース消化履歴情報を取得
            List<ConsumptionCourse> consumptionCourseList = ConsumptionCourse.getConsumptionCourseList(con, this.getShop().getShopID(), this.getSlipNo());
            for (ConsumptionCourse consumptionCourse : consumptionCourseList) {
                DataSalesDetail dsd = new DataSalesDetail();
                //Luc start add 20150225 Bug #35220
                dsd.loadForConsumption(this.getShop().getShopID(), consumptionCourse.getSlipNo(), consumptionCourse.getContractNo(),consumptionCourse.getContractDetailNo(), consumptionCourse.getContractShopId());
                //Luc end add 20150225 Bug #35220
                dsd.setConsumptionCourse(6, consumptionCourse);
                dsd.setConsumptionNum(consumptionCourse.getConsumptionNum());

                MstStaff staff = new MstStaff();
                staff.setStaffID(consumptionCourse.getStaffId());
                staff.load(con);
                dsd.setStaff(staff);
                // vtbphuong start add 20150605 
                dsd.setProductNum(consumptionCourse.getConsumptionNum().intValue());
                // vtbphuong end add 20150605 
                this.add(dsd);
            }
        }

        rs.close();

        this.loadDiscount(con);

        this.loadPayments(con);

        if (payments.size() == 0) {
            DataPayment dp = new DataPayment();
            dp.setPaymentNo(0);
            this.addPayment(dp);
        }

        // 開拓者情報ロード
        loadPioneerCode(con);

        return true;
    }

    /**
     * 顧客別の伝票データを全て読み込む。
     *
     * @param con ConnectionWrapper
     * @param paymentNo 支払No.
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean loadCustomerAll(ConnectionWrapper con) throws SQLException {
        this.clear();
        //add by ltthuc 2014/06/18
        ResultSetWrapper rs = null;
        if ((SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev")) && SystemInfo.getSetteing().isUsePrepaid()) {
            rs = con.executeQuery(this.getSelectCustomerAllSQL2());
        } else {
            rs = con.executeQuery(this.getSelectCustomerAllSQL());
        }
        //end add by ltthuc
        //nhanvt start edit 20150327 Bug #35748
        DataSalesDetail dsd = null;
        while (rs.next()) {
            dsd = new DataSalesDetail();
            dsd.setAccountSetting(this.getAccountSetting());
            dsd.setData(rs);
            this.add(dsd);
        }
        //nhanvt end edit 20150327 Bug #35748
        rs.close();

        return true;
    }
    
    // IVS_Thanh start add 2014/07/11 Mashu_お会計表示
    public boolean loadDataSaleMainStaff(ConnectionWrapper con
            ) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getSelecDataSaleMainStaffSQL());
        while (rs.next()) {
            DataSalesMainstaff dsm = new DataSalesMainstaff();
            dsm.setData(rs);
            this.salesMainStaffs.add(dsm);
        }
        rs.close();
        return true;
        
     }
     
     private String getSelecDataSaleMainStaffSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select \n");
         sql.append("     drm.*,staff_no,msc.shop_class_name \n");
         sql.append(" from \n");
         sql.append("    mst_shop_category msc \n");
         sql.append("    left join  data_sales_mainstaff drm using(shop_category_id)  \n");
         sql.append("    left join mst_staff ms using(staff_id) \n");
         sql.append(" where \n");
         sql.append("     drm.delete_date is null \n");
         sql.append("     and drm.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()));
         sql.append("     and drm.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
         sql.append(" order by \n");
         sql.append("      shop_category_id \n"); 
         return sql.toString();
     }
     
     public boolean loadDataReservaionMainStaff(ConnectionWrapper con,
            Integer reservationNo) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getSelecDatatReservationMainStaffSQL(reservationNo));
        while (rs.next()) {
            DataSalesMainstaff dsm = new DataSalesMainstaff();
            dsm.setData(rs);
            this.salesMainStaffs.add(dsm);
        }
        rs.close();
        return true;
        
     }
     
     private String getSelecDatatReservationMainStaffSQL(Integer reservationNo) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select \n");
         sql.append("     drm.*,staff_no,msc.shop_class_name \n");
         sql.append(" from \n");
         sql.append("    mst_shop_category msc \n");
         sql.append("    left join  data_reservation_mainstaff drm using(shop_category_id)  \n");
         sql.append("    left join mst_staff ms using(staff_id) \n");
         sql.append(" where \n");
         sql.append("     drm.delete_date is null \n");
         sql.append("     and drm.reservation_no = " + SQLUtil.convertForSQL(reservationNo));
         sql.append("     and drm.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
         sql.append(" order by \n");
         sql.append("      shop_category_id \n"); 
         return sql.toString();
     }
     // IVS_Thanh end add 2014/07/11 Mashu_お会計表示

    /**
     * 予約データを読み込む。
     *
     * @param con ConnectionWrapper
     * @param reservationNo 予約No.のリスト
     * @throws java.sql.SQLException SQLException
     * @return true - 成功
     */
    public boolean loadReservation(ConnectionWrapper con,
            Integer reservationNo) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getSelectReservationSQL(reservationNo));

        while (rs.next()) {
            MstStaff ms;
            if (this.size() == 0) {
                this.getShop().setShopID(rs.getInt("shop_id"));
                this.setSalesDate(rs.getDate("sales_date"));

                Integer flg = rs.getInt("mobile_flag");
                if (!rs.wasNull()) {
                    this.setMobileFlag(flg);
                }
                flg = rs.getInt("next_flag");
                if (!rs.wasNull()) {
                    this.setNextFlag(flg);
                }
                flg = rs.getInt("preorder_flag");
                if (!rs.wasNull()) {
                    this.setPreorderFlag(flg);
                }

                this.setReservationDatetime(rs.getTimestamp("reservation_datetime"));
                MstCustomer cus = new MstCustomer();
                cus.setCustomerID(rs.getInt("customer_id"));
                cus.setCustomerNo(rs.getString("customer_no"));
                cus.setCustomerName(0, rs.getString("customer_name1"));
                cus.setCustomerName(1, rs.getString("customer_name2"));
                MstSosiaCustomer msc = new MstSosiaCustomer();
                msc.setSosiaID(rs.getInt("sosia_id"));
                cus.setSosiaCustomer(msc);
                this.setCustomer(cus);
                this.setDesignated(rs.getBoolean("charge_staff_designated_flag"));
                ms = new MstStaff();
                ms.setStaffID(rs.getInt("charge_staff_id"));
                ms.setStaffNo(rs.getString("charge_staff_no"));
                ms.setStaffName(0, rs.getString("charge_staff_name1"));
                ms.setStaffName(1, rs.getString("charge_staff_name2"));
                this.setStaff(ms);
            }

            DataSalesDetail dsd = new DataSalesDetail();
            dsd.setShop(this.getShop());
            // start add 20130806 nakhoa
            Object reserShop = rs.getObject("contract_shop_id");
            if (reserShop != null) {
                dsd.setContractShopId(rs.getInt("contract_shop_id"));
            } else {
                dsd.setContractShopId(null);
            }
            // end add 20130806 nakhoa
            dsd.setProductDivision(1);

            Object courseFlg = rs.getObject("course_flg");
            if (courseFlg == null) {
                //技術の場合
                ProductClass pc = new ProductClass();
                pc.setProductClassID(rs.getInt("technic_class_id"));
                pc.setProductClassName(rs.getString("technic_class_name"));
                // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                pc.setShopCategoryID(rs.getInt("shop_category_id"));
                // IVS_Thanh end add 2014/07/14 Mashu_お会計表示

                Product p = new Product();
                p.setProductClass(pc);
                p.setProductID(rs.getInt("technic_id"));
                p.setProductNo(rs.getString("technic_no"));
                p.setProductName(rs.getString("technic_name"));
                p.setPrice(rs.getLong("price"));
                p.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                p.setPraiseTime(rs.getBoolean("is_praise_time"));
                p.setOperationTime(rs.getInt("operation_time"));

                dsd.setProduct(p);
                dsd.setProductNum(1);
                dsd.setProductValue(p.getPrice());
            } else if ((Integer) courseFlg == 1) {
                //コース契約の場合
                CourseClass courseClass = new CourseClass();
                courseClass.setCourseClassId(rs.getInt("technic_class_id"));
                courseClass.setCourseClassName(rs.getString("technic_class_name"));
                courseClass.setDisplaySeq(rs.getInt("display_seq"));
                // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                courseClass.setShopCategoryID(rs.getInt("shop_category_id"));
                // IVS_Thanh end add 2014/07/14 Mashu_お会計表示

                Course course = new Course();
                course.setCourseClass(courseClass);
                course.setCourseId(rs.getInt("technic_id"));
                course.setCourseName(rs.getString("technic_name"));
                course.setBasePrice(rs.getLong("base_price"));
                course.setPrice(rs.getLong("price"));
                course.setNum(rs.getInt("course_num"));
                course.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                course.setIsPraiseTime(rs.getBoolean("is_praise_time"));
                course.setOperationTime(rs.getInt("operation_time"));

                dsd.setCourse(5, course);
                dsd.setProductNum(1);
            } else if ((Integer) courseFlg == 2) {
                //消化コースの場合
                ConsumptionCourseClass consumptionCourseClass = new ConsumptionCourseClass();
                consumptionCourseClass.setCourseClassId(rs.getInt("technic_class_id"));
                consumptionCourseClass.setCourseClassName(rs.getString("technic_class_name"));
                consumptionCourseClass.setDisplaySeq(rs.getInt("display_seq"));
                // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
                consumptionCourseClass.setShopCategoryID(rs.getInt("shop_category_id"));
                // IVS_Thanh end add 2014/07/14 Mashu_お会計表示

                ConsumptionCourse consumptionCourse = new ConsumptionCourse();
                consumptionCourse.setConsumptionCourseClass(consumptionCourseClass);
                consumptionCourse.setShopId(this.getShop().getShopID());
                consumptionCourse.setContractNo(rs.getInt("contract_no"));
                consumptionCourse.setContractDetailNo(rs.getInt("contract_detail_no"));
                consumptionCourse.setCourseId(rs.getInt("technic_id"));
                consumptionCourse.setCourseName(rs.getString("technic_name"));
//                            consumptionCourse.setPrice(rs.getLong("price"));
                consumptionCourse.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
                consumptionCourse.setPraiseTime(rs.getBoolean("is_praise_time"));
                consumptionCourse.setOperationTime(rs.getInt("operation_time"));

                consumptionCourse.setContractShopId(rs.getInt("contract_shop_id"));

                //ConsumptionCourse tmpConsumptionCourse = ConsumptionCourse.getConsumptionNum(con, consumptionCourse.getShopId(), consumptionCourse.getContractNo(), consumptionCourse.getContractDetailNo());
                ConsumptionCourse tmpConsumptionCourse = ConsumptionCourse.getConsumptionNum(con, consumptionCourse.getContractShopId(), consumptionCourse.getContractNo(), consumptionCourse.getContractDetailNo());
                consumptionCourse.setNum(tmpConsumptionCourse.getNum());

                long price = 0;
                if (tmpConsumptionCourse.getPrice() != null && tmpConsumptionCourse.getNum() != null) {
                    price = new BigDecimal(tmpConsumptionCourse.getPrice()).divide(new BigDecimal(tmpConsumptionCourse.getNum()), 0, RoundingMode.UP).longValue();
                }
                consumptionCourse.setPrice(price);

                consumptionCourse.setConsumptionNum(tmpConsumptionCourse.getConsumptionNum());
                consumptionCourse.setConsumptionRestNum(tmpConsumptionCourse.getConsumptionRestNum());

                dsd.setConsumptionCourse(6, consumptionCourse);
                dsd.setConsumptionNum(1d);
            }

            dsd.setDesignated(rs.getBoolean("designated_flag"));
            ms = new MstStaff();
            ms.setStaffID(rs.getInt("staff_id"));
            ms.setStaffNo(rs.getString("staff_no"));
            ms.setStaffName(0, rs.getString("staff_name1"));
            ms.setStaffName(1, rs.getString("staff_name2"));
            dsd.setStaff(ms);
            MstBed bed = new MstBed();
            bed.setBedID(rs.getInt("bed_id"));
            bed.setBedName(rs.getString("bed_name"));
            dsd.setBed(bed);
            //Start add 20131031 lvut 
            MstResponse response = new MstResponse();
            response.setResponseID(rs.getInt("response_id1"));
            try {
                response.load(con);
            } catch (Exception e) {
            }
            this.setResponse1(response);
            response = new MstResponse();
            response.setResponseID(rs.getInt("response_id2"));
            try {
                response.load(con);
            } catch (Exception e) {
            }
            this.setResponse2(response);
            //End add 20131031 lvut 

            // 按分有の店舗の場合には按分データを取得する
            if (SystemInfo.getCurrentShop().isProportionally()) {
                this.loadReservationProportionally(con, dsd, reservationNo, rs.getInt("reservation_detail_no"));
            }
            //nhanvt start edit 20141216 Bug #33936
            boolean flag = false;
            int index = -1;
            if(this.size() >0){
                for(DataSalesDetail dsdTmp : this){
                    index++;
                    if(dsdTmp!= null && dsd!= null){
                        if( dsdTmp.getProductDivision()==6 && dsd.getProductDivision() ==6 && (Integer) courseFlg == 2
                                ){
                            if(dsdTmp.getConsumptionCourse().getContractShopId().equals(dsd.getConsumptionCourse().getContractShopId())&& dsdTmp.getConsumptionCourse().getContractNo().equals(dsd.getConsumptionCourse().getContractNo())
                                && dsdTmp.getConsumptionCourse().getContractDetailNo().equals(dsd.getConsumptionCourse().getContractDetailNo())){
                                flag = true;
                                break;
                            }
                            
                        }
                    }
                    
                }

                if(flag){
                    this.get(index).setConsumptionNum(this.get(index).getConsumptionNum()+1);
                }else{
                    this.add(dsd);
                }
            }else{
                 this.add(dsd);
            }
            //nhanvt end edit 20141216 Bug #33936
            
            
        }
        
        rs.close();

        return true;
    }

    /**
     * 按分データを取得する
     *
     * @param con ConnectionWrapper
     * @param paymentNo 伝票明細
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean loadSlipProportionally(ConnectionWrapper con, DataSalesDetail dsd) throws SQLException {
        dsd.clear();
        ResultSetWrapper rs = con.executeQuery(this.getSelectSlipProportionallySQL(dsd));

        while (rs.next()) {
            DataSalesProportionally dsp = new DataSalesProportionally();
            dsp.setDataSalesDetail(dsd);
            DataProportionally proportionally = new DataProportionally();
            proportionally.setDataProportionallyID(rs.getInt("data_proportionally_id"));
            MstProportionally mp = new MstProportionally();
            mp.setProportionallyID(rs.getInt("proportionally_id"));
            mp.setProportionallyName(rs.getString("proportionally_name"));
            mp.setProportionallyPoint(rs.getInt("proportionally_point"));
            mp.setDisplaySeq(rs.getInt("display_seq"));
            proportionally.setProportionally(mp);
            proportionally.setRatio(rs.getInt("proportionally_ratio"));
            dsp.setProportionally(proportionally);
            dsp.setDesignated(rs.getBoolean("designated_flag"));
            MstStaff staff = new MstStaff();
            staff.setStaffID(rs.getInt("staff_id"));
            staff.setStaffNo(rs.getString("staff_no"));
            staff.setStaffName(0, "staff_name1");
            staff.setStaffName(1, "staff_name2");
            dsp.setStaff(staff);
            dsp.setPoint(rs.getInt("point"));
            dsp.setRate(rs.getInt("ratio"));
            dsd.add(dsp);
        }

        rs.close();

        return true;
    }

    /**
     * 按分データを取得する
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean loadReservationProportionally(ConnectionWrapper con, DataSalesDetail dsd, Integer reservationNo, Integer reservationDetailNo) throws SQLException {
        dsd.clear();
        ResultSetWrapper rs = con.executeQuery(this.getSelectReservationProportionallySQL(dsd, reservationNo, reservationDetailNo));

        while (rs.next()) {
            DataSalesProportionally dsp = new DataSalesProportionally();
            dsp.setDataSalesDetail(dsd);
            DataProportionally proportionally = new DataProportionally();
            proportionally.setDataProportionallyID(rs.getInt("data_proportionally_id"));
            MstProportionally mp = new MstProportionally();
            mp.setProportionallyID(rs.getInt("proportionally_id"));
            mp.setProportionallyName(rs.getString("proportionally_name"));
            mp.setProportionallyPoint(rs.getInt("proportionally_point"));
            mp.setDisplaySeq(rs.getInt("display_seq"));
            proportionally.setProportionally(mp);
            proportionally.setRatio(rs.getInt("proportionally_ratio"));
            dsp.setProportionally(proportionally);
            dsp.setDesignated(rs.getBoolean("designated_flag"));
            MstStaff staff = new MstStaff();
            staff.setStaffID(rs.getInt("staff_id"));
            staff.setStaffNo(rs.getString("staff_no"));
            staff.setStaffName(0, "staff_name1");
            staff.setStaffName(1, "staff_name2");
            dsp.setStaff(staff);
            dsp.setPoint(rs.getInt("proportionally_point"));
            dsp.setRate(rs.getInt("proportionally_ratio"));
            dsd.add(dsp);
        }

        rs.close();

        return true;
    }

    /**
     * 割引データを読み込む。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    private boolean loadDiscount(ConnectionWrapper con) throws SQLException {
        discount = new DataSalesDetail();
        discount.setProductDivision(0);

        ResultSetWrapper rs = con.executeQuery(this.getSelectDiscountSQL());

        if (rs.next()) {
            discount.setDiscountData(rs);
        }

        rs.close();

        return true;
    }

    public boolean loadPayments(ConnectionWrapper con) throws SQLException {
        payments.clear();

        ResultSetWrapper rs = con.executeQuery(this.getSelectPaymentsSQL());

        while (rs.next()) {
            DataPayment dp = new DataPayment();
            dp.setData(rs);
            payments.add(dp);
        }

        rs.close();

        for (DataPayment dp : payments) {
            dp.loadAll(con);
        }

        return true;
    }

    /**
     * 売掛金が存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @param slipNo 伝票No.
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public static boolean isExistCollectedBill(ConnectionWrapper con,
            Integer shopID, Integer slipNo) throws SQLException {
        boolean result = false;

        if (slipNo == null || slipNo < 1) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(DataSales.getExistCollectedBillSQL(shopID, slipNo));

        if (rs.next()) {
            result = (0 < rs.getInt("cnt"));
        }

        rs.close();

        return result;
    }

    /**
     * 売掛金が存在するかチェックする。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     * @throws java.sql.SQLException 例外
     */
    public boolean isExistCollectedBill(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        if (this.getSlipNo() == null || this.getSlipNo() < 1) {
            return false;
        }

        if (con == null) {
            return false;
        }

        ResultSetWrapper rs = con.executeQuery(this.getExistCollectedBillSQL(
                this.getShop().getShopID(), this.getSlipNo()));

        if (rs.next()) {
            result = (0 < rs.getInt("cnt"));
        }

        rs.close();

        return result;
    }

    /**
     *
     * @param con
     * @return
     */
    public boolean delete(ConnectionWrapper con) throws SQLException {
        boolean result = false;

        result = (con.executeUpdate(this.getDeleteSQL()) == 1);

        return result;
    }

    /**
     * 売上ヘッダに付随する伝票系ののデータを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deleteSlipChildren(ConnectionWrapper con) throws SQLException {
        //売上明細を削除
        if (!this.deleteDetail(con)) {
            return false;
        }

        //売上按分明細を削除
        if (!this.deleteProportionally(con)) {
            return false;
        }

        //2016/09/05 GB MOD #54426 Start
        // 商品シェア情報は削除しない
//        //按分情報（商品シェア）
//        if (!this.deleteDetailProportionally(con)) {
//            return false;
//        }
        //2016/09/05 GB MOD #54426 End

        //支払を削除
        if (!this.deletePayment(con)) {
            return false;
        }

        //支払明細を削除
        if (!this.deletePaymentDetail(con)) {
            return false;
        }
        
        // IVS_Thanh start add 2014/07/10 Mashu_お会計表示
        if (!this.deleteDataSaleMainStaff(con)) {
            return false;
        }
        // IVS_Thanh end add 2014/07/10 Mashu_お会計表示

//                //コース契約を削除
//                if(!this.deleteDataContract(con))
//                    return false;
//
//                //コース消化を削除
//                if(!this.deleteDataContractDigestion(con))
//                    return false;
        
        return true;
    }

    /**
     * 売上ヘッダに付随する全てのデータを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deleteAllChildren(ConnectionWrapper con) throws SQLException {
        //売上明細を削除
        if (!this.deleteSlipChildren(con)) {
            return false;
        }

        //予約を削除
        if (!this.deleteReservation(con)) {
            return false;
        }

        //予約明細を削除
        if (!this.deleteReservationDetail(con)) {
            return false;
        }

        //ポイント情報を削除
        this.deletePoint(con);

        //プリペイド情報を削除
        this.deletePrepaid(con);

        //コース契約を削除
        //this.deleteDataContractLogically(con);
        this.deleteDataContract(con);


        //コース消化を削除
        //this.deleteDataContractDigestionLogically(con);
        this.deleteDataContractDigestion(con);
        
        //IVS_LVTu start add 2015/12/01 New request #44111
        //update slip_no null
        if (!this.deleteDataProposal(con)) {
                return false;
        }
        //IVS_LVTu end add 2015/12/01 New request #44111
        return true;
    }

    /**
     * 売上ヘッダと付随する全てのデータを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deleteAll(ConnectionWrapper con) throws SQLException {
        //売上明細、支払、支払明細、予約
        if (!this.deleteAllChildren(con)) {
            return false;
        }
        //売上ヘッダ
        if (!this.delete(con)) {
            return false;
        }

        return true;
    }

    /**
     * 伝票明細データを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deleteDetail(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteDetailSQL());
    }

    /**
     * 伝票按分明細データを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deleteProportionally(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteProportionallySQL());
    }

    /**
     * 按分情報（商品シェア）を削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deleteDetailProportionally(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteDetailProportionallySQL());
    }

    /**
     * 支払データを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deletePayment(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeletePaymentSQL());
    }

    /**
     * 支払明細データを削除する。
     *
     * @param con ConnectionWrapper
     * @return true - 成功
     */
    public boolean deletePaymentDetail(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeletePaymentDetailSQL());
    }

    public boolean deleteReservation(ConnectionWrapper con) throws SQLException {
        //IVS_LVTu start edit 2015/10/26 Bug #43929
        //return 0 <= con.executeUpdate(this.getDeleteReservationSQL());
        return 0 <= con.executeUpdate(this.getDeleteReservationLogicallySQL());
        //IVS_LVTu end edit 2015/10/26 Bug #43929
    }

    public boolean deleteReservationDetail(ConnectionWrapper con) throws SQLException {
        //IVS_LVTu start edit 2015/10/26 Bug #43929
        //return 0 <= con.executeUpdate(this.getDeleteReservationDetailSQL());
        return 0 <= con.executeUpdate(this.getDeleteReservationDetailLogicallySQL());
        //IVS_LVTu end edit 2015/10/26 Bug #43929
    }

    public boolean deletePoint(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeletePointSQL());
    }

    public boolean deletePrepaid(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeletePrepaidSQL());
    }
    
    // IVS_Thanh start add 2014/07/10 Mashu_お会計表示
    public boolean deleteDataSaleMainStaff(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteDataSaleMainStaffSQL());
    }
    // IVS_Thanh end add 2014/07/10 Mashu_お会計表示
    
    public boolean deleteDataProposal(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteSlipNoDataproposalSQL());
    }
    
    public boolean updateDataProposal(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getUpdateSlipNoDataproposalSQL());
    }

    /**
     * コース契約削除
     *
     * @param con
     * @return
     * @throws SQLException
     */
    public boolean deleteDataContract(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteDataContractSQL());
    }

    /**
     * コース契約削除
     *
     * @param con
     * @return
     * @throws SQLException
     */
    public boolean deleteDataContractLogically(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteDataContractLogicallySQL());
    }

    /**
     * コース消化削除
     *
     * @param con
     * @return
     * @throws SQLException
     */
    public boolean deleteDataContractDigestion(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteDataContractDigestionSQL());
    }

    /**
     * コース消化削除
     *
     * @param con
     * @return
     * @throws SQLException
     */
    public boolean deleteDataContractDigestionLogically(ConnectionWrapper con) throws SQLException {
        return 0 <= con.executeUpdate(this.getDeleteDataContractDigestionLogicallySQL());
    }

    /**
     * 新しい伝票No.を取得するＳＱＬ文を取得する。
     *
     * @return 新しい伝票No.を取得するＳＱＬ文
     */
    public String getNewSlipNoSQL() {
        return "select coalesce(max(slip_no), 0) + 1 as new_slip_no\n"
                + "from data_sales\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n";
    }

    /**
     * 伝票ヘッダを取得するＳＱＬ文を取得する。
     *
     * @return 伝票ヘッダを取得するＳＱＬ文
     */
    public String getSelectSQL() {
        return "select\n"
                + "ds.shop_id,\n"
                + "ds.slip_no,\n"
                + "ds.sales_date,\n"
                + "ds.customer_id,\n"
                + "ds.visit_num,\n"
                + "ds.designated_flag as charge_staff_designated_flag,\n"
                + "ds.staff_id as charge_staff_id,\n"
                + "ds.visited_memo,\n"
                + "mc.customer_no,\n"
                + "mc.customer_name1,\n"
                + "mc.customer_name2\n"
                + "from data_sales ds\n"
                + "left outer join mst_customer mc\n"
                + "on mc.customer_id = ds.customer_id\n"
                + "where ds.delete_date is null\n"
                + "and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * 伝票ヘッダをInsertするＳＱＬ文を取得する。
     *
     * @return 伝票ヘッダをInsertするＳＱＬ文
     */
    public String getInsertSQL() {
        return "insert into data_sales\n"
                + "(shop_id, slip_no, sales_date, customer_id,\n"
                + "designated_flag, staff_id,\n"
                + (type == 3 ? "visit_num,\n" : "")
                + "visited_memo, next_visit_date, insert_date, update_date, delete_date)\n"
                + "values(\n"
                + SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n"
                + (tempFlag ? null : SQLUtil.convertForSQLDateOnly(this.getSalesDate())) + ",\n"
                + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n"
                + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + (this.getStaff() == null ? "null" : SQLUtil.convertForSQL(this.getStaff().getStaffID())) + ",\n"
                + (type == 3 ? "(select count(*) + 1\n"
                + "from data_sales\n"
                + "where delete_date is null\n"
                + "and sales_date < " + SQLUtil.convertForSQL(this.getSalesDate()) + "\n"
                + "and customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ")"
                + "+(select coalesce(before_visit_num,0) from mst_customer where customer_id="+ SQLUtil.convertForSQL(this.getCustomer().getCustomerID())+")"
                + ",\n" : "")
                + SQLUtil.convertForSQL(this.getVisitedMemo()) + "\n,"
                + SQLUtil.convertForSQL(this.getNextVisitDate()) + "\n,"
                + "current_timestamp, current_timestamp, null)\n";
    }

    /**
     * 伝票ヘッダをUpdateするＳＱＬ文を取得する。
     *
     * @return 伝票ヘッダをUpdateするＳＱＬ文
     */
    public String getUpdateSQL() {
        return "update data_sales\n"
                + "set\n"
                + "sales_date = " + (tempFlag ? null
                : SQLUtil.convertForSQLDateOnly(this.getSalesDate())) + ",\n"
                + "customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ",\n"
                + "designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + (this.getStaff() == null ? "" : "staff_id = " + SQLUtil.convertForSQL(this.getStaff().getStaffID()) + ",\n")
                + (type == 3 ? "visit_num = (select count(*) + 1\n"
                + "from data_sales\n"
                + "where delete_date is null\n"
                + "and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and sales_date < " + SQLUtil.convertForSQL(this.getSalesDate()) + "\n"
                + "and customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()) + ")"
                + "+(select coalesce(before_visit_num,0) from mst_customer where customer_id="+ SQLUtil.convertForSQL(this.getCustomer().getCustomerID())+")"
                + ",\n" : "")
                + "visited_memo = " + SQLUtil.convertForSQL(this.getVisitedMemo()) + "\n,"
                + "next_visit_date = " + SQLUtil.convertForSQL(this.getNextVisitDate()) + "\n,"
                + "update_date = current_timestamp\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * 伝票ヘッダ、伝票明細を取得するＳＱＬ文を取得する。
     *
     * @return 伝票ヘッダ、伝票明細を取得するＳＱＬ文
     */
    public String getSelectAllSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ds.shop_id");
        sql.append("     ,msh.shop_name");
        sql.append("     ,msh.open_hour");
        sql.append("     ,msh.open_minute");
        sql.append("     ,msh.close_hour");
        sql.append("     ,msh.close_minute");
        sql.append("     ,msh.display_proportionally");
        sql.append("     ,ds.slip_no");
        sql.append("     ,ds.sales_date");
        sql.append("     ,ds.customer_id");
        sql.append("     ,mc.customer_no");
        sql.append("     ,mc.customer_name1");
        sql.append("     ,mc.customer_name2");
        sql.append("     ,mc.sosia_id");
        sql.append("     ,ds.designated_flag as charge_staff_designated_flag");
        sql.append("     ,ds.staff_id as charge_staff_id");
        sql.append("     ,ds.visited_memo");
        sql.append("     ,ds.next_visit_date");
        sql.append("     ,cms.staff_no as charge_staff_no");
        sql.append("     ,cms.staff_name1 as charge_staff_name1");
        sql.append("     ,cms.staff_name2 as charge_staff_name2");

        if (type == 3) {
            sql.append(" ,visit_num");
        }

        sql.append("     ,dsd.slip_detail_no");
        sql.append("     ,dsd.product_division");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_class_id");
        sql.append("         when 2 then mi.item_class_id");
        sql.append("         when 3 then mt.technic_class_id");
        sql.append("         when 4 then mi.item_class_id");
        sql.append("         when 5 then mcc.course_class_id");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcc.course_class_id");
        sql.append("         when 8 then mcc.course_class_id");
        sql.append("         when 9 then mcc.course_class_id");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else -1");
        sql.append("      end as product_class_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.technic_class_name");
        sql.append("         when 2 then mic.item_class_name");
        sql.append("         when 3 then mtc.technic_class_name");
        sql.append("         when 4 then mic.item_class_name");
        sql.append("         when 5 then mcc.course_class_name");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcc.course_class_name");
        sql.append("         when 8 then mcc.course_class_name");
        sql.append("         when 9 then mcc.course_class_name");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else ''");
        sql.append("      end as product_class_name");
        //Luc start add 20150817 #41948
        sql.append("      ,CASE dsd.product_division \n");
        sql.append("         WHEN 1 THEN mtc.shop_category_id \n");
        sql.append("         WHEN 2 THEN mic.shop_category_id \n");
        sql.append("         WHEN 3 THEN mtc.shop_category_id \n");
        sql.append("         WHEN 4 THEN mic.shop_category_id \n");
        sql.append("         WHEN 5 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 6 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 7 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 8 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 9 THEN mcc.shop_category_id \n");
        sql.append("         ELSE -1 \n");
        sql.append("      END AS shop_category_id \n");
        //Luc end add 20150817 #41948
        sql.append("     ,dsd.product_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_no");
        sql.append("         when 2 then mi.item_no");
        sql.append("         when 3 then mt.technic_no");
        sql.append("         when 4 then mi.item_no");
        sql.append("         when 5 then ''");
        sql.append("                else ''");
        sql.append("      end as product_no");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_name || case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1)  then '（予約用）' else '' end");
        sql.append("         when 2 then mi.item_name");
        sql.append("         when 3 then mt.technic_name || case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1) then '（予約用）' else '' end");
        sql.append("         when 4 then mi.item_name");
        sql.append("         when 5 then mcs.course_name");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcs.course_name");
        sql.append("         when 8 then mcs.course_name");
        sql.append("         when 9 then mcs.course_name");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else ''");
        sql.append("      end as product_name");
        sql.append("     ,dsd.product_num");
        sql.append("     ,dsd.product_value");
        sql.append("     ,dsd.discount_rate");
        sql.append("     ,dsd.discount_value");
        sql.append("     ,dsd.designated_flag");
        sql.append("     ,dsd.approached_flag");
        sql.append("     ,dsd.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,mt.praise_time_limit");
        sql.append("     ,mt.is_praise_time");

        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.operation_time");
        sql.append("         when 2 then null");
        sql.append("         when 3 then null");
        sql.append("         when 4 then null");
        sql.append("         when 5 then mcs.operation_time");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcs.operation_time");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else null");

        sql.append("      end as operation_time");


        sql.append("     ,get_tax_rate(ds.sales_date) as tax_rate");
        // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.shop_category_id");
        sql.append("         when 2 then mic.shop_category_id");
        sql.append("         when 3 then mtc.shop_category_id");
        sql.append("         when 4 then mic.shop_category_id");
        sql.append("         when 5 then mcc.shop_category_id");        
        sql.append("         when 7 then mcc.shop_category_id");
        sql.append("         when 8 then mcc.shop_category_id");
        sql.append("         when 9 then mcc.shop_category_id");        
        sql.append("                else 0");
        sql.append("      end as shop_category_id");
        // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
        // IVS SANG START INSERT 20131011
        sql.append("     ,mcs.operation_time as operation_time");
        // IVS SANG END INSERT 20131011
        
         // vtbphuong start add 20150518 
          sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.display_seq");
        sql.append("         when 2 then mic.display_seq");
        sql.append("         when 3 then mtc.display_seq");
        sql.append("         when 4 then mic.display_seq");
        sql.append("         when 5 then mcc.display_seq");
        sql.append("         when 7 then null");
        sql.append("                else null");

        sql.append("      end as class_display_seq");
        // vtbphuong end add 20150518 
        
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         left join mst_staff cms");
        sql.append("                on cms.staff_id = ds.staff_id");
        sql.append("         left join mst_shop msh");
        sql.append("                on msh.shop_id = ds.shop_id");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = ds.customer_id");
        sql.append("         left join data_sales_detail dsd");
        sql.append("                on dsd.shop_id = ds.shop_id");
        sql.append("               and dsd.slip_no = ds.slip_no");
        sql.append("               and dsd.product_division not in (0, 6)");
        sql.append("               and dsd.delete_date is null");
        sql.append("         left join mst_technic mt");
        sql.append("                on mt.technic_id = dsd.product_id");
        sql.append("         left join mst_technic_class mtc");
        sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        sql.append("         left join mst_item mi");
        sql.append("                on mi.item_id = dsd.product_id");
        sql.append("         left join mst_item_class mic");
        sql.append("                on mic.item_class_id = mi.item_class_id");
        sql.append("         left join mst_course mcs");
        sql.append("                on mcs.course_id = dsd.product_id");
        sql.append("         left join mst_course_class mcc");
        sql.append("                on mcc.course_class_id = mcs.course_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = dsd.staff_id");
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "");
        sql.append("     and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "");
        sql.append(" order by");
        sql.append("     dsd.slip_detail_no");

        return sql.toString();
    }

    public String getSelectAllSQL2() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ds.shop_id");
        sql.append("     ,msh.shop_name");
        sql.append("     ,msh.open_hour");
        sql.append("     ,msh.open_minute");
        sql.append("     ,msh.close_hour");
        sql.append("     ,msh.close_minute");
        sql.append("     ,msh.display_proportionally");
        sql.append("     ,ds.slip_no");
        sql.append("     ,ds.sales_date");
        sql.append("     ,ds.customer_id");
        sql.append("     ,mc.customer_no");
        sql.append("     ,mc.customer_name1");
        sql.append("     ,mc.customer_name2");
        sql.append("     ,mc.sosia_id");
        sql.append("     ,ds.designated_flag as charge_staff_designated_flag");
        sql.append("     ,ds.staff_id as charge_staff_id");
        sql.append("     ,ds.visited_memo");
        sql.append("     ,ds.next_visit_date");
        sql.append("     ,cms.staff_no as charge_staff_no");
        sql.append("     ,cms.staff_name1 as charge_staff_name1");
        sql.append("     ,cms.staff_name2 as charge_staff_name2");
        //add by ltthuc 2014/06/13
        sql.append("     ,mi.prepa_id");
        sql.append("     ,mi.prepaid_price");
        sql.append("     ,mic.prepa_class_id");
        //end add by ltthuc
        if (type == 3) {
            sql.append(" ,visit_num");
        }

        sql.append("     ,dsd.slip_detail_no");
        sql.append("     ,dsd.product_division");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_class_id");
        sql.append("         when 2 then mi.item_class_id");
        sql.append("         when 3 then mt.technic_class_id");
        sql.append("         when 4 then mi.item_class_id");
        sql.append("         when 5 then mcc.course_class_id");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcc.course_class_id");
        sql.append("         when 8 then mcc.course_class_id");
        sql.append("         when 9 then mcc.course_class_id");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else -1");
        sql.append("      end as product_class_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.technic_class_name");
        sql.append("         when 2 then mic.item_class_name");
        sql.append("         when 3 then mtc.technic_class_name");
        sql.append("         when 4 then mic.item_class_name");
        sql.append("         when 5 then mcc.course_class_name");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcc.course_class_name");
        sql.append("         when 8 then mcc.course_class_name");
        sql.append("         when 9 then mcc.course_class_name");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else ''");
        sql.append("      end as product_class_name");
        //Luc start add 20150817 #41948
        sql.append("      ,CASE dsd.product_division \n");
        sql.append("         WHEN 1 THEN mtc.shop_category_id \n");
        sql.append("         WHEN 2 THEN mic.shop_category_id \n");
        sql.append("         WHEN 3 THEN mtc.shop_category_id \n");
        sql.append("         WHEN 4 THEN mic.shop_category_id \n");
        sql.append("         WHEN 5 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 6 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 7 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 8 THEN mcc.shop_category_id \n");
        sql.append("         WHEN 9 THEN mcc.shop_category_id \n");
        sql.append("         ELSE -1 \n");
        sql.append("      END AS shop_category_id \n");
        //Luc end add 20150817 #41948
        sql.append("     ,dsd.product_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_no");
        sql.append("         when 2 then mi.item_no");
        sql.append("         when 3 then mt.technic_no");
        sql.append("         when 4 then mi.item_no");
        sql.append("         when 5 then ''");
        sql.append("                else ''");
        sql.append("      end as product_no");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_name || case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1)  then '（予約用）' else '' end");
        sql.append("         when 2 then mi.item_name");
        sql.append("         when 3 then mt.technic_name || case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1) then '（予約用）' else '' end");
        sql.append("         when 4 then mi.item_name");
        sql.append("         when 5 then mcs.course_name");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcs.course_name");
        sql.append("         when 8 then mcs.course_name");
        sql.append("         when 9 then mcs.course_name");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else ''");
        sql.append("      end as product_name");
        sql.append("     ,dsd.product_num");
        sql.append("     ,dsd.product_value");
        sql.append("     ,dsd.discount_rate");
        sql.append("     ,dsd.discount_value");
        sql.append("     ,dsd.designated_flag");
        sql.append("     ,dsd.approached_flag");
        sql.append("     ,dsd.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,mt.praise_time_limit");
        sql.append("     ,mt.is_praise_time");

        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.operation_time");
        sql.append("         when 2 then null");
        sql.append("         when 3 then null");
        sql.append("         when 4 then null");
        sql.append("         when 5 then mcs.operation_time");
        //START ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("         when 7 then mcs.operation_time");
        //END ADD 20131105 IVS NGUYEN TAN LUC
        sql.append("                else null");

        sql.append("      end as operation_time");


        sql.append("     ,get_tax_rate(ds.sales_date) as tax_rate");
        // IVS SANG START INSERT 20131011
        sql.append("     ,mcs.operation_time as operation_time");
        // IVS SANG END INSERT 20131011
        
        // vtbphuong start add 20150518 
          sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.display_seq");
        sql.append("         when 2 then mic.display_seq");
        sql.append("         when 3 then mtc.display_seq");
        sql.append("         when 4 then mic.display_seq");
        sql.append("         when 5 then mcc.display_seq");
        sql.append("         when 7 then null");
        sql.append("                else null");

        sql.append("      end as class_display_seq");
        // vtbphuong end add 20150518 
        
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         left join mst_staff cms");
        sql.append("                on cms.staff_id = ds.staff_id");
        sql.append("         left join mst_shop msh");
        sql.append("                on msh.shop_id = ds.shop_id");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = ds.customer_id");
        sql.append("         left join data_sales_detail dsd");
        sql.append("                on dsd.shop_id = ds.shop_id");
        sql.append("               and dsd.slip_no = ds.slip_no");
        sql.append("               and dsd.product_division not in (0, 6)");
        sql.append("               and dsd.delete_date is null");
        sql.append("         left join mst_technic mt");
        sql.append("                on mt.technic_id = dsd.product_id");
        sql.append("         left join mst_technic_class mtc");
        sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        sql.append("         left join mst_item mi");
        sql.append("                on mi.item_id = dsd.product_id");
        sql.append("         left join mst_item_class mic");
        sql.append("                on mic.item_class_id = mi.item_class_id");
        sql.append("         left join mst_course mcs");
        sql.append("                on mcs.course_id = dsd.product_id");
        sql.append("         left join mst_course_class mcc");
        sql.append("                on mcc.course_class_id = mcs.course_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = dsd.staff_id");
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ds.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "");
        sql.append("     and ds.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "");
        sql.append(" order by");
        sql.append("     dsd.slip_detail_no");

        return sql.toString();
    }

    /**
     * 顧客別の伝票ヘッダ、伝票明細を取得するＳＱＬ文を取得する。
     *
     * @return 顧客別の伝票ヘッダ、伝票明細を取得するＳＱＬ文
     */
    public String getSelectCustomerAllSQL() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ds.shop_id");
        sql.append("     ,msh.shop_name");
        sql.append("     ,ds.slip_no");
        sql.append("     ,ds.sales_date");
        sql.append("     ,ds.customer_id");
        sql.append("     ,mc.customer_no");
        sql.append("     ,mc.customer_name1");
        sql.append("     ,mc.customer_name2");
        sql.append("     ,mc.sosia_id");
        sql.append("     ,ds.designated_flag as charge_staff_designated_flag");
        sql.append("     ,ds.staff_id as charge_staff_id");
        sql.append("     ,ds.visited_memo");
        sql.append("     ,cms.staff_no as charge_staff_no");
        sql.append("     ,cms.staff_name1 as charge_staff_name1");
        sql.append("     ,cms.staff_name2 as charge_staff_name2");

        if (type == 3) {
            sql.append(" ,visit_num");
        }

        sql.append("     ,dsd.slip_detail_no");
        sql.append("     ,dsd.product_division");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_class_id");
        sql.append("         when 2 then mi.item_class_id");
        sql.append("         when 3 then mt.technic_class_id");
        sql.append("         when 4 then mi.item_class_id");
        sql.append("                else -1");
        sql.append("      end as product_class_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.technic_class_name");
        sql.append("         when 2 then mic.item_class_name");
        sql.append("         when 3 then mtc.technic_class_name");
        sql.append("         when 4 then mic.item_class_name");
        sql.append("                else ''");
        sql.append("      end as product_class_name");
        sql.append("     ,dsd.product_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_no");
        sql.append("         when 2 then mi.item_no");
        sql.append("         when 3 then mt.technic_no");
        sql.append("         when 4 then mi.item_no");
        sql.append("                else ''");
        sql.append("      end as product_no");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_name");
        sql.append("         when 2 then mi.item_name");
        sql.append("         when 3 then mt.technic_name");
        sql.append("         when 4 then mi.item_name");
        sql.append("                else ''");
        sql.append("      end as product_name");
        sql.append("     ,dsd.product_num");
        sql.append("     ,dsd.product_value");
        sql.append("     ,dsd.discount_rate");
        sql.append("     ,dsd.discount_value");
        sql.append("     ,dsd.designated_flag");
        sql.append("     ,dsd.approached_flag");
        sql.append("     ,dsd.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,mt.praise_time_limit");
        sql.append("     ,mt.is_praise_time");
        sql.append("     ,get_tax_rate(ds.sales_date) as tax_rate");
        //VUINV start add 20140721 お会計表示 #27721
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.shop_category_id");
        sql.append("         when 3 then mtc.shop_category_id");
        sql.append("         when 2 then mic.shop_category_id");
        sql.append("         when 4 then mic.shop_category_id");
        sql.append("         when 5 then mcoc.shop_category_id");
        sql.append("         when 6 then mcoc.shop_category_id");
        sql.append("                else 0");
        sql.append("      end as shop_category_id");
        //VUINV end add 20140721 お会計表示 #27721
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         left join mst_staff cms");
        sql.append("                on cms.staff_id = ds.staff_id");
        sql.append("         left join mst_shop msh");
        sql.append("                on msh.shop_id = ds.shop_id");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = ds.customer_id");
        sql.append("         left join data_sales_detail dsd");
        sql.append("                on dsd.shop_id = ds.shop_id");
        sql.append("               and dsd.slip_no = ds.slip_no");
        sql.append("               and dsd.product_division != 0");
        sql.append("               and dsd.delete_date is null");
        sql.append("         left join mst_technic mt");
        sql.append("                on mt.technic_id = dsd.product_id");
        sql.append("         left join mst_technic_class mtc");
        sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        sql.append("         left join mst_item mi");
        sql.append("                on mi.item_id = dsd.product_id");
        sql.append("         left join mst_item_class mic");
        sql.append("                on mic.item_class_id = mi.item_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = dsd.staff_id");
        //VUINV start add 20140721 お会計表示 #27721
        sql.append("         left join mst_course mco");
        sql.append("                on mco.course_id = dsd.product_id");
        sql.append("         left join mst_course_class mcoc");
        sql.append("                on mcoc.course_class_id = mco.course_class_id");
        //VUINV end add 20140721 お会計表示 #27721
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ds.sales_date is not null");
        sql.append("     and ds.customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()));
        sql.append(" order by");
        sql.append("      ds.sales_date desc");
        sql.append("     ,ds.slip_no");
        sql.append("     ,dsd.slip_detail_no");

        return sql.toString();
    }
    // add by ltthuc 2014/06/18   

    public String getSelectCustomerAllSQL2() {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append("      ds.shop_id");
        sql.append("     ,msh.shop_name");
        sql.append("     ,ds.slip_no");
        sql.append("     ,ds.sales_date");
        sql.append("     ,ds.customer_id");
        sql.append("     ,mc.customer_no");
        sql.append("     ,mc.customer_name1");
        sql.append("     ,mc.customer_name2");
        sql.append("     ,mc.sosia_id");
        sql.append("     ,ds.designated_flag as charge_staff_designated_flag");
        sql.append("     ,ds.staff_id as charge_staff_id");
        sql.append("     ,ds.visited_memo");
        sql.append("     ,cms.staff_no as charge_staff_no");
        sql.append("     ,cms.staff_name1 as charge_staff_name1");
        sql.append("     ,cms.staff_name2 as charge_staff_name2");
        //add by ltthuc 2014/06/18
        sql.append("     ,mi.prepa_id");
        sql.append("     ,mi.prepaid_price");
        sql.append("     ,mic.prepa_class_id");
        //end add by ltthuc
        if (type == 3) {
            sql.append(" ,visit_num");
        }

        sql.append("     ,dsd.slip_detail_no");
        sql.append("     ,dsd.product_division");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_class_id");
        sql.append("         when 2 then mi.item_class_id");
        sql.append("         when 3 then mt.technic_class_id");
        sql.append("         when 4 then mi.item_class_id");
        sql.append("                else -1");
        sql.append("      end as product_class_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.technic_class_name");
        sql.append("         when 2 then mic.item_class_name");
        sql.append("         when 3 then mtc.technic_class_name");
        sql.append("         when 4 then mic.item_class_name");
        sql.append("                else ''");
        sql.append("      end as product_class_name");
        sql.append("     ,dsd.product_id");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_no");
        sql.append("         when 2 then mi.item_no");
        sql.append("         when 3 then mt.technic_no");
        sql.append("         when 4 then mi.item_no");
        sql.append("                else ''");
        sql.append("      end as product_no");
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mt.technic_name");
        sql.append("         when 2 then mi.item_name");
        sql.append("         when 3 then mt.technic_name");
        sql.append("         when 4 then mi.item_name");
        sql.append("                else ''");
        sql.append("      end as product_name");
        sql.append("     ,dsd.product_num");
        sql.append("     ,dsd.product_value");
        sql.append("     ,dsd.discount_rate");
        sql.append("     ,dsd.discount_value");
        sql.append("     ,dsd.designated_flag");
        sql.append("     ,dsd.approached_flag");
        sql.append("     ,dsd.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,mt.praise_time_limit");
        sql.append("     ,mt.is_praise_time");
        sql.append("     ,get_tax_rate(ds.sales_date) as tax_rate");
        //VUINV start add 20140721 お会計表示 #27721
        sql.append("     ,case dsd.product_division");
        sql.append("         when 1 then mtc.shop_category_id");
        sql.append("         when 3 then mtc.shop_category_id");
        sql.append("         when 2 then mic.shop_category_id");
        sql.append("         when 4 then mic.shop_category_id");
        sql.append("         when 5 then mcoc.shop_category_id");
        sql.append("         when 6 then mcoc.shop_category_id");
        sql.append("                else 0");
        sql.append("      end as shop_category_id");
        //VUINV end add 20140721 お会計表示 #27721
        sql.append(" from");
        sql.append("     data_sales ds");
        sql.append("         left join mst_staff cms");
        sql.append("                on cms.staff_id = ds.staff_id");
        sql.append("         left join mst_shop msh");
        sql.append("                on msh.shop_id = ds.shop_id");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = ds.customer_id");
        sql.append("         left join data_sales_detail dsd");
        sql.append("                on dsd.shop_id = ds.shop_id");
        sql.append("               and dsd.slip_no = ds.slip_no");
        sql.append("               and dsd.product_division != 0");
        sql.append("               and dsd.delete_date is null");
        sql.append("         left join mst_technic mt");
        sql.append("                on mt.technic_id = dsd.product_id");
        sql.append("         left join mst_technic_class mtc");
        sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        sql.append("         left join mst_item mi");
        sql.append("                on mi.item_id = dsd.product_id");
        sql.append("         left join mst_item_class mic");
        sql.append("                on mic.item_class_id = mi.item_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = dsd.staff_id");
        //VUINV start add 20140721 お会計表示 #27721
        sql.append("         left join mst_course mco");
        sql.append("                on mco.course_id = dsd.product_id");
        sql.append("         left join mst_course_class mcoc");
        sql.append("                on mcoc.course_class_id = mco.course_class_id");
        //VUINV end add 20140721 お会計表示 #27721
        sql.append(" where");
        sql.append("         ds.delete_date is null");
        sql.append("     and ds.sales_date is not null");
        sql.append("     and ds.customer_id = " + SQLUtil.convertForSQL(this.getCustomer().getCustomerID()));
        sql.append(" order by");
        sql.append("      ds.sales_date desc");
        sql.append("     ,ds.slip_no");
        sql.append("     ,dsd.slip_detail_no");

        return sql.toString();
    }
    //end add by ltthuc

    /**
     * 伝票ヘッダ、伝票明細を取得するＳＱＬ文を取得する。
     *
     * @return 伝票ヘッダ、伝票明細を取得するＳＱＬ文
     */
    public String getSelectSlipProportionallySQL(DataSalesDetail dsd) {
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
                + "and dsp.shop_id = " + SQLUtil.convertForSQL(dsd.getShop().getShopID()) + "\n"
                + "and dsp.slip_no = " + SQLUtil.convertForSQL(dsd.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(dsd.getSlipDetailNo()) + "\n"
                + "order by\n"
                + "mp.display_seq, dsp.data_proportionally_id\n"
                + ";\n";
    }

    /**
     * 伝票ヘッダ、伝票明細を取得するＳＱＬ文を取得する。
     *
     * @return 伝票ヘッダ、伝票明細を取得するＳＱＬ文
     */
    public String getSelectReservationProportionallySQL(DataSalesDetail dsd, Integer reservationNo, Integer reservationDetailNo) {
        return "select\n"
                + "mp.*,\n"
                + "ms.staff_id,\n"
                + "ms.staff_no,\n"
                + "ms.staff_name1,\n"
                + "ms.staff_name2,\n"
                + "dp.*,\n"
                + "drp.designated_flag\n"
                + "from\n"
                + "data_reservation_proportionally as drp\n"
                + "inner join data_proportionally as dp on\n"
                + "dp.data_proportionally_id = drp.data_proportionally_id\n"
                + "inner join mst_proportionally as mp on\n"
                + "mp.proportionally_id = dp.proportionally_id\n"
                + "left outer join mst_staff as ms on\n"
                + "ms.staff_id = drp.staff_id\n"
                + "where\n"
                + "drp.delete_date is null\n"
                + "and drp.shop_id = " + SQLUtil.convertForSQL(dsd.getShop().getShopID()) + "\n"
                + "and drp.reservation_no = " + SQLUtil.convertForSQL(reservationNo) + "\n"
                + "and drp.reservation_detail_no = " + SQLUtil.convertForSQL(reservationDetailNo) + "\n"
                + "order by\n"
                + "mp.display_seq, drp.data_proportionally_id\n"
                + ";\n";
    }

    /**
     * 割引データを取得するＳＱＬ文を取得する。
     *
     * @return 割引データを取得するＳＱＬ文
     */
    public String getSelectDiscountSQL() {
        return "select dsd.shop_id,\n"
                + "dsd.slip_no,\n"
                + "dsd.slip_detail_no,\n"
                + "dsd.product_division,\n"
                + "dsd.product_id,\n"
                + "0 as product_no,\n"
                + "md.discount_name,\n"
                + "dsd.product_value,\n"
                + "dsd.discount_rate,\n"
                + "dsd.discount_value,\n"
                + "dsd.staff_id,\n"
                + "ms.staff_no,\n"
                + "ms.staff_name1,\n"
                + "ms.staff_name2,\n"
                + "md.discount_division\n"
                + "from data_sales_detail dsd\n"
                + "left outer join mst_discount md\n"
                + "on md.discount_id = dsd.product_id\n"
                + "left outer join mst_staff ms\n"
                + "on ms.staff_id = dsd.staff_id\n"
                + "where dsd.delete_date is null\n"
                + "and dsd.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and dsd.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and dsd.product_division = 0\n";
    }

    public String getSelectPaymentsSQL() {
        return "select dp.*, ms.staff_name1, ms.staff_name2\n"
                + "from data_payment dp\n"
                + "left outer join mst_staff ms\n"
                + "on ms.staff_id = dp.staff_id\n"
                + "where dp.delete_date is null\n"
                + "and dp.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and dp.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * 売掛金が存在するかを取得するＳＱＬ文を取得する。
     *
     * @param slipNo 売上No.
     * @return 売掛金が存在するかを取得するＳＱＬ文
     */
    public static String getExistCollectedBillSQL(Integer shopID, Integer slipNo) {
        return "select count(*) as cnt\n"
                + "from data_sales ds\n"
                + "inner join data_payment dp\n"
                + "on dp.shop_id = ds.shop_id\n"
                + "and dp.slip_no = ds.slip_no\n"
                + "and dp.payment_no > 0\n"
                + "and dp.delete_date is null\n"
                + "where ds.delete_date is null\n"
                + "and ds.shop_id = " + SQLUtil.convertForSQL(shopID) + "\n"
                + "and ds.slip_no = " + SQLUtil.convertForSQL(slipNo) + "\n";
    }

    /**
     * 売上ヘッダデータを削除するＳＱＬ文を取得する。
     *
     * @return 売上ヘッダデータを削除するＳＱＬ文
     */
    public String getDeleteSQL() {
        return "update data_sales\n"
                + "set update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where delete_date is null\n"
                + "and shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * 売上明細データを削除するＳＱＬ文を取得する。
     *
     * @return 売上明細データを削除するＳＱＬ文
     */
    public String getDeleteDetailSQL() {
        return "delete from data_sales_detail\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
        /*return	"update data_sales_detail\n" +
         "set update_date = current_timestamp,\n" +
         "delete_date = current_timestamp\n" +
         "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
         "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";*/
    }

    /**
     * 売上按分明細データを削除するＳＱＬ文を取得する。
     *
     * @return 売上按分明細データを削除するＳＱＬ文
     */
    public String getDeleteProportionallySQL() {
        return "update data_sales_proportionally\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * 按分情報（商品シェア）を削除するＳＱＬ文を取得する。
     *
     * @return 按分情報（商品シェア）を削除するＳＱＬ文
     */
    public String getDeleteDetailProportionallySQL() {
        return "delete from data_sales_detail_proportionally\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }
        
    //2016/09/05 GB MOD #54426 Start 
    /**
     * 按分情報（商品シェア）を更新する。
     *
     * @param con ConnectionWrapper
     * @param dsd 伝票詳細
     * @return true - 成功
     */
    public boolean updateDetailProportionally(ConnectionWrapper con, DataSalesDetail dsd) throws SQLException {
        return 0 <= con.executeUpdate(this.getUpdateDetailProportionallySQL(dsd));
    }
    
    /**
     * 按分情報（商品シェア）を更新するＳＱＬ文を取得する。
     *
     * @param dsd 伝票詳細
     * @return 按分情報（商品シェア）を更新するＳＱＬ文
     */    
     public String getUpdateDetailProportionallySQL(DataSalesDetail dsd) {
        return "update data_sales_detail_proportionally\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "staff_id = " + SQLUtil.convertForSQL(dsd.getStaff().getStaffID()) + ",\n"
                + "approached_flag = " + SQLUtil.convertForSQL(dsd.getDesignated()) + "\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(dsd.getSlipDetailNo()) + "\n"
                + "and seq_num = 1\n";
    }
    //2016/09/05 GB MOD #54426 End
     
    /**
     * 支払データを削除するＳＱＬ文を取得する。
     *
     * @return 支払データを削除するＳＱＬ文
     */
    public String getDeletePaymentSQL() {
        return "delete from data_payment\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and payment_no >= 0\n";
    }

    /**
     * 支払明細データを削除するＳＱＬ文を取得する。
     *
     * @return 支払明細データを削除するＳＱＬ文
     */
    public String getDeletePaymentDetailSQL() {
        return "delete from data_payment_detail\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }
    
    // IVS_Thanh start add 2014/07/10 Mashu_お会計表示
    public String getDeleteDataSaleMainStaffSQL() {
        return "delete from data_sales_mainstaff\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }
    // IVS_Thanh start add 2014/07/10 Mashu_お会計表示
    
    public String getUpdateSlipNoDataproposalSQL() {
        return "update data_proposal\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "contract_shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n"
                + "slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and proposal_id = " + SQLUtil.convertForSQL(this.getProposalID()) + "\n";
    }
    
    public String getDeleteSlipNoDataproposalSQL() {
        return "update data_proposal\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "contract_shop_id = null , \n"
                + "slip_no = null \n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * 予約データを削除するＳＱＬ文を取得する。
     *
     * @return 予約データを削除するＳＱＬ文
     */
    public String getDeleteReservationSQL() {
        return "delete from  data_reservation\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and delete_date is null\n";
    }

    /**
     * 予約明細データを削除するＳＱＬ文を取得する。
     *
     * @return 予約明細データを削除するＳＱＬ文
     */
    public String getDeleteReservationDetailSQL() {
        return "delete from  data_reservation_detail\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and reservation_no in (\n"
                + "select reservation_no\n"
                + "from data_reservation\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + ")\n"
                + "and delete_date is null\n";
    }
    
    /**
    * 削除用Update文を取得する。
    * @return 
    */
   private String getDeleteReservationLogicallySQL()
   {
        return	"update data_reservation\n" +
                "set\n" +
                "delete_date = current_timestamp\n" +
                "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n" +
                "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n" +
                "and delete_date is null\n";
   }

   public String getDeleteReservationDetailLogicallySQL() {
        return "update  data_reservation_detail\n"
                +"set\n" 
                +"delete_date = current_timestamp\n" 
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and reservation_no in (\n"
                + "select reservation_no\n"
                + "from data_reservation\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + ")\n"
                + "and delete_date is null\n";
    }
   
    /**
     * ポイント履歴データを削除するＳＱＬ文を取得する。
     *
     * @return ポイント履歴データを削除するＳＱＬ文
     */
    public String getDeletePointSQL() {
        return "update data_point\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * プリペイド履歴データを削除するＳＱＬ文を取得する。
     *
     * @return プリペイド履歴データを削除するＳＱＬ文
     */
    public String getDeletePrepaidSQL() {
        return "update data_prepaid\n"
                + "set\n"
                + "update_date = current_timestamp,\n"
                + "delete_date = current_timestamp\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * コース契約履歴データを削除するＳＱＬ文を取得する。
     *
     * @return コース契約履歴データを削除するＳＱＬ文
     */
    public String getDeleteDataContractSQL() {
        return "delete from  data_contract\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
    }

    /**
     * コース契約履歴データを論理削除するＳＱＬ文を取得する。
     *
     * @return コース契約履歴データを論理削除するＳＱＬ文
     */
    public String getDeleteDataContractLogicallySQL() {
        return "update data_contract\n"
                + "set delete_date = current_timestamp "
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
    }

    /**
     * コース消化履歴データを削除するＳＱＬ文を取得する。
     *
     * @return コース消化履歴データを削除するＳＱＬ文
     */
    public String getDeleteDataContractDigestionSQL() {
        return "delete from  data_contract_digestion\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
    }

    /**
     * コース消化履歴データを論理削除するＳＱＬ文を取得する。
     *
     * @return コース消化履歴データを論理削除するＳＱＬ文
     */
    public String getDeleteDataContractDigestionLogicallySQL() {
        return "update data_contract_digestion\n"
                + "set delete_date = current_timestamp "
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo());
    }

    /**
     * 予約データを読み込むＳＱＬ文を取得する。
     *
     * @param reservationNo 予約No.のリスト
     * @return 予約データを読み込むＳＱＬ文
     */
    private String getSelectReservationSQL(Integer reservationNo) {
        StringBuilder sql = new StringBuilder(1000);
        //技術
        sql.append(" select");
        sql.append("      dr.shop_id");
        // start add 20130806 nakhoa
        sql.append("      ,drd.contract_shop_id");
        // end add 20130806 nakhoa
        sql.append("     ,drd.reservation_detail_no");
        sql.append("     ,drd.reservation_datetime");
        sql.append("     ,case when");
        sql.append("             date_part('hour', drd.reservation_datetime) < msh.open_hour");
        sql.append("         or (date_part('hour', drd.reservation_datetime) = msh.open_hour");
        sql.append("                 and date_part('minute', drd.reservation_datetime) < msh.open_minute");
        sql.append("         )");
        sql.append("         then drd.reservation_datetime - cast('1 days' as interval)");
        sql.append("         else drd.reservation_datetime");
        sql.append("      end as sales_date");
        sql.append("     ,dr.customer_id");
        sql.append("     ,mc.customer_no");
        sql.append("     ,mc.customer_name1");
        sql.append("     ,mc.customer_name2");
        sql.append("     ,mc.sosia_id");
        sql.append("     ,dr.designated_flag as charge_staff_designated_flag");
        sql.append("     ,cms.staff_id as charge_staff_id");
        sql.append("     ,cms.staff_no as charge_staff_no");
        sql.append("     ,cms.staff_name1 as charge_staff_name1");
        sql.append("     ,cms.staff_name2 as charge_staff_name2");
        sql.append("     ,mtc.technic_class_id");
        sql.append("     ,mtc.technic_class_name");
        sql.append("     ,mtc.display_seq");
        // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
        sql.append("     ,mtc.shop_category_id");
        // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
        sql.append("     ,mt.technic_id");
        sql.append("     ,mt.technic_no");
        sql.append("     ,mt.technic_name || case when (mt.technic_no like 'mo-rsv-%') or (mt.mobile_flag=1) then '（予約用）' else '' end as technic_name");
        sql.append("     ,mt.price as base_price");
        sql.append("     ,mup.price");
        sql.append("     ,mt.praise_time_limit");
        sql.append("     ,mt.is_praise_time");
        sql.append("     ,mt.operation_time as operation_time");
        sql.append("     ,0 as course_num");
        sql.append("     ,drd.designated_flag");
        sql.append("     ,drd.bed_id");
        sql.append("     ,mb.bed_name");
        sql.append("     ,ms.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,dr.mobile_flag");
        sql.append("     ,dr.next_flag");
        sql.append("     ,dr.preorder_flag");
        sql.append("     ,null as course_flg");
        sql.append("     ,drd.contract_no");
        sql.append("     ,drd.contract_detail_no");
        sql.append("     ,0 as contract_shop_id");
        //Start add 20131031 lvut
        sql.append("     ,dr.response_id1\n");
        sql.append("     ,dr.response_id2\n");
        //End add 20131031 lvut
        sql.append(" from");
        sql.append("     data_reservation dr");
        sql.append("         left join mst_shop msh");
        sql.append("                on msh.shop_id = dr.shop_id");
        sql.append("         inner join data_reservation_detail drd");
        sql.append("                 on drd.shop_id = dr.shop_id");
        sql.append("                and drd.reservation_no = dr.reservation_no");
        sql.append("                and drd.delete_date is null");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = dr.customer_id");
        sql.append("         left join mst_staff cms");
        sql.append("                on cms.staff_id = dr.staff_id");
        sql.append("         left join mst_use_product mup");
        sql.append("                on mup.product_id = drd.technic_id");
        sql.append("               and mup.shop_id = drd.shop_id");
        sql.append("               and mup.product_division = 1");
        sql.append("         left join mst_technic mt");
        sql.append("                on mt.technic_id = drd.technic_id");
        sql.append("         left join mst_technic_class mtc");
        sql.append("                on mtc.technic_class_id = mt.technic_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = drd.staff_id");
        sql.append("         left join mst_bed mb");
        sql.append("                on mb.shop_id = dr.shop_id");
        sql.append("               and mb.bed_id = drd.bed_id");
        sql.append(" where");
        sql.append("         dr.delete_date is null");
        sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and dr.reservation_no = " + reservationNo.toString());
        sql.append("     and drd.course_flg is null");


        //コース契約
        sql.append(" union all");
        sql.append(" select");
        sql.append("      dr.shop_id");
        // start add 20130806 nakhoa
        sql.append("      ,drd.contract_shop_id");
        // end add 20130806 nakhoa
        sql.append("     ,drd.reservation_detail_no");
        sql.append("     ,drd.reservation_datetime");
        sql.append("     ,case when");
        sql.append("             date_part('hour', drd.reservation_datetime) < msh.open_hour");
        sql.append("         or (date_part('hour', drd.reservation_datetime) = msh.open_hour");
        sql.append("                 and date_part('minute', drd.reservation_datetime) < msh.open_minute");
        sql.append("         )");
        sql.append("         then drd.reservation_datetime - cast('1 days' as interval)");
        sql.append("         else drd.reservation_datetime");
        sql.append("      end as sales_date");
        sql.append("     ,dr.customer_id");
        sql.append("     ,mc.customer_no");
        sql.append("     ,mc.customer_name1");
        sql.append("     ,mc.customer_name2");
        sql.append("     ,mc.sosia_id");
        sql.append("     ,dr.designated_flag as charge_staff_designated_flag");
        sql.append("     ,cms.staff_id as charge_staff_id");
        sql.append("     ,cms.staff_no as charge_staff_no");
        sql.append("     ,cms.staff_name1 as charge_staff_name1");
        sql.append("     ,cms.staff_name2 as charge_staff_name2");
        sql.append("     ,cc.course_class_id as technic_class_id");
        sql.append("     ,cc.course_class_name as technic_class_name");
        sql.append("     ,cc.display_seq");
        // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
        sql.append("     ,cc.shop_category_id");
        // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
        sql.append("     ,c.course_id as technic_id");
        sql.append("     ,null as technic_no");
        sql.append("     ,c.course_name as technic_name");
        sql.append("     ,c.price as base_price");
        sql.append("     ,mup.price");
        sql.append("     ,c.praise_time_limit as praise_time_limit");
        sql.append("     ,c.is_praise_time as is_praise_time");
        sql.append("     ,c.operation_time as operation_time");
        sql.append("     ,c.num as course_num");
        sql.append("     ,drd.designated_flag");
        sql.append("     ,drd.bed_id");
        sql.append("     ,mb.bed_name");
        sql.append("     ,ms.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,dr.mobile_flag");
        sql.append("     ,dr.next_flag");
        sql.append("     ,dr.preorder_flag");
        sql.append("     ,1 as course_flg");
        sql.append("     ,drd.contract_no");
        sql.append("     ,drd.contract_detail_no");
        sql.append("     ,0 as contract_shop_id");
        //Start add 20131031 lvut
        sql.append("     ,dr.response_id1\n");
        sql.append("     ,dr.response_id2\n");
        //End add 20131031 lvut
        sql.append(" from");
        sql.append("     data_reservation dr");
        sql.append("         left join mst_shop msh");
        sql.append("                on msh.shop_id = dr.shop_id");
        sql.append("         inner join data_reservation_detail drd");
        sql.append("                 on drd.shop_id = dr.shop_id");
        sql.append("                and drd.reservation_no = dr.reservation_no");
        sql.append("                and drd.delete_date is null");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = dr.customer_id");
        sql.append("         left join mst_staff cms");
        sql.append("                on cms.staff_id = dr.staff_id");
        sql.append("         left join mst_use_product mup");
        sql.append("                on mup.product_id = drd.technic_id");
        sql.append("               and mup.shop_id = drd.shop_id");
        sql.append("               and mup.product_division = 3");
        sql.append("         left join mst_course c");
        sql.append("                on c.course_id = drd.technic_id");
        sql.append("         left join mst_course_class cc");
        sql.append("                on cc.course_class_id = c.course_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = drd.staff_id");
        sql.append("         left join mst_bed mb");
        sql.append("                on mb.shop_id = dr.shop_id");
        sql.append("               and mb.bed_id = drd.bed_id");
        sql.append(" where");
        sql.append("         dr.delete_date is null");
        sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and dr.reservation_no = " + reservationNo.toString());
        sql.append("     and drd.course_flg = 1");



        //消化コース
        sql.append(" union all");
        sql.append(" select distinct");
        sql.append("      dr.shop_id");
        // start add 20130806 nakhoa
        sql.append("      ,drd.contract_shop_id");
        // end add 20130806 nakhoa
        sql.append("     ,drd.reservation_detail_no");
        sql.append("     ,drd.reservation_datetime");
        sql.append("     ,case when");
        sql.append("             date_part('hour', drd.reservation_datetime) < msh.open_hour");
        sql.append("         or (date_part('hour', drd.reservation_datetime) = msh.open_hour");
        sql.append("                 and date_part('minute', drd.reservation_datetime) < msh.open_minute");
        sql.append("         )");
        sql.append("         then drd.reservation_datetime - cast('1 days' as interval)");
        sql.append("         else drd.reservation_datetime");
        sql.append("      end as sales_date");
        sql.append("     ,dr.customer_id");
        sql.append("     ,mc.customer_no");
        sql.append("     ,mc.customer_name1");
        sql.append("     ,mc.customer_name2");
        sql.append("     ,mc.sosia_id");
        sql.append("     ,dr.designated_flag as charge_staff_designated_flag");
        sql.append("     ,cms.staff_id as charge_staff_id");
        sql.append("     ,cms.staff_no as charge_staff_no");
        sql.append("     ,cms.staff_name1 as charge_staff_name1");
        sql.append("     ,cms.staff_name2 as charge_staff_name2");
        sql.append("     ,cc.course_class_id as technic_class_id");
        sql.append("     ,cc.course_class_name as technic_class_name");
        sql.append("     ,cc.display_seq");
        // IVS_Thanh start add 2014/07/14 Mashu_お会計表示
        sql.append("     ,cc.shop_category_id");
        // IVS_Thanh end add 2014/07/14 Mashu_お会計表示
        sql.append("     ,c.course_id as technic_id");
        sql.append("     ,null as technic_no");
        sql.append("     ,c.course_name as technic_name");
        sql.append("     ,c.price as base_price");
        sql.append("     ,mup.price");
        sql.append("     ,c.praise_time_limit as praise_time_limit");
        sql.append("     ,c.is_praise_time as is_praise_time");
        sql.append("     ,c.operation_time as operation_time");
        sql.append("     ,0 as course_num");
        sql.append("     ,drd.designated_flag");
        sql.append("     ,drd.bed_id");
        sql.append("     ,mb.bed_name");
        sql.append("     ,ms.staff_id");
        sql.append("     ,ms.staff_no");
        sql.append("     ,ms.staff_name1");
        sql.append("     ,ms.staff_name2");
        sql.append("     ,dr.mobile_flag");
        sql.append("     ,dr.next_flag");
        sql.append("     ,dr.preorder_flag");
        sql.append("     ,2 as course_flg");
        sql.append("     ,drd.contract_no");
        sql.append("     ,drd.contract_detail_no");
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             dc.shop_id");
        sql.append("         from");
        sql.append("             data_sales ds");
        sql.append("                 join data_contract dc");
        sql.append("                     using (shop_id, slip_no)");
        sql.append("         where");
        sql.append("                 ds.customer_id = dr.customer_id");
        sql.append("             and dc.contract_no = drd.contract_no");
        sql.append("             and dc.contract_detail_no = drd.contract_detail_no");
        sql.append("      ) as contract_shop_id");
        //Start add 20131031 lvut
        sql.append("     ,dr.response_id1\n");
        sql.append("     ,dr.response_id2\n");
        //End add 20131031 lvut
        sql.append(" from");
        sql.append("     data_reservation dr");
        sql.append("         left join mst_shop msh");
        sql.append("                on msh.shop_id = dr.shop_id");
        sql.append("         inner join data_reservation_detail drd");
        sql.append("                 on drd.shop_id = dr.shop_id");
        sql.append("                and drd.reservation_no = dr.reservation_no");
        sql.append("                and drd.delete_date is null");
        sql.append("         left join mst_customer mc");
        sql.append("                on mc.customer_id = dr.customer_id");
        sql.append("         left join mst_staff cms");
        sql.append("                on cms.staff_id = dr.staff_id");
        sql.append("         left join mst_use_product mup");
        sql.append("                on mup.product_id = drd.technic_id");
        sql.append("               and mup.shop_id = drd.shop_id");
        sql.append("               and mup.product_division = 3");
        
        //--Luc end edit 20141205 Bug #33551
        //sql.append("         left join data_contract_digestion dcd");
        //sql.append("                on drd.shop_id = dcd.shop_id and drd.contract_no = dcd.contract_no and drd.contract_detail_no = dcd.contract_detail_no");
        sql.append("         inner JOIN data_contract dcd ON drd.contract_shop_id = dcd.shop_id\n");
        sql.append("         AND drd.contract_no = dcd.contract_no\n");
        sql.append("         AND drd.contract_detail_no = dcd.contract_detail_no and dcd.delete_date is null and drd.technic_id = dcd.product_id \n");
        //--Luc end edit 20141205 Bug #33551
        sql.append("         left join mst_course c");
        sql.append("                on c.course_id = drd.technic_id");
        sql.append("         left join mst_course_class cc");
        sql.append("                on cc.course_class_id = c.course_class_id");
        sql.append("         left join mst_staff ms");
        sql.append("                on ms.staff_id = drd.staff_id");
        sql.append("         left join mst_bed mb");
        sql.append("                on mb.shop_id = dr.shop_id");
        sql.append("               and mb.bed_id = drd.bed_id");
        sql.append(" where");
        sql.append("         dr.delete_date is null");
        sql.append("     and dr.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append("     and dr.reservation_no = " + reservationNo.toString());
        sql.append("     and drd.course_flg = 2");


        sql.append(" order by");
        sql.append("      reservation_detail_no");
        sql.append("     ,technic_class_id");
        sql.append("     ,technic_id");

        return sql.toString();
    }

    public void setPioneerCode(Integer intPioneerCode) {
        m_intPioneerCode = intPioneerCode;
    }

    public Integer getPioneerCode() {
        return m_intPioneerCode;
    }

    private void registerPioneer(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rsPioneer;
        String strExecute;
        Integer intShopid;
        Integer intSlipno;
        Integer intStaffid;

        intShopid = getShop().getShopID();
        intSlipno = getSlipNo();
        intStaffid = getPioneerCode();


        rsPioneer = con.executeQuery(getSQLSelectPioneer(intShopid, intSlipno));

        if (rsPioneer.next()) {
            strExecute = getSQLUpdatePioneer(intShopid, intSlipno, intStaffid);
        } else {
            strExecute = getSQLInsertPioneer(intShopid, intSlipno, intStaffid);
        }

        rsPioneer.close();

        con.executeUpdate(strExecute);
    }

    private String getSQLSelectPioneer(Integer intShopid, Integer intSlipno) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" select");
        sql.append(" 	staff_id");
        sql.append(" from data_pioneer");

        sql.append(" where");
        sql.append(" 	delete_date is null");
        sql.append(" and	shop_id = " + SQLUtil.convertForSQL(intShopid));
        sql.append(" and	slip_no = " + SQLUtil.convertForSQL(intSlipno));

        return sql.toString();
    }

    private String getSQLInsertPioneer(Integer intShopid, Integer intSlipno, Integer intStaffid) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" insert into data_pioneer (");
        sql.append(" 	shop_id");
        sql.append(" ,	slip_no");
        sql.append(" ,	staff_id");
        sql.append(" ,	insert_date");
        sql.append(" ,	update_date");
        sql.append(" )");
        sql.append(" values (");
        sql.append(" 	" + SQLUtil.convertForSQL(intShopid));
        sql.append(" ,	" + SQLUtil.convertForSQL(intSlipno));
        sql.append(" ,	" + SQLUtil.convertForSQL(intStaffid));
        sql.append(" ,	now()");
        sql.append(" ,	now()");
        sql.append(" )");

        return sql.toString();
    }

    private String getSQLUpdatePioneer(Integer intShopid, Integer intSlipno, Integer intStaffid) {
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" update data_pioneer set");
        sql.append(" 	staff_id = " + SQLUtil.convertForSQL(intStaffid));
        sql.append(" ,	update_date = now()");

        sql.append(" where");
        sql.append(" 	delete_date is null");
        sql.append(" and	shop_id = " + SQLUtil.convertForSQL(intShopid));
        sql.append(" and	slip_no = " + SQLUtil.convertForSQL(intSlipno));

        return sql.toString();
    }

    private void loadPioneerCode(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rsPioneer;

        rsPioneer = con.executeQuery(getSQLSelectPioneer(getShop().getShopID(), getSlipNo()));

        if (rsPioneer.next()) {
            setPioneerCode(rsPioneer.getInt("staff_id"));
        } else {
            setPioneerCode(null);
        }

        rsPioneer.close();
    }

    public Integer getMobileFlag() {
        return mobileFlag;
    }

    public void setMobileFlag(Integer mobileFlag) {
        this.mobileFlag = mobileFlag;
    }

    public Integer getNextFlag() {
        return nextFlag;
    }

    public void setNextFlag(Integer nextFlag) {
        this.nextFlag = nextFlag;
    }

    public Integer getPreorderFlag() {
        return preorderFlag;
    }

    public void setPreorderFlag(Integer preorderFlag) {
        this.preorderFlag = preorderFlag;
    }

    /**
     * @return the newAccount
     */
    public boolean isNewAccount() {
        return newAccount;
    }

    /**
     * @param newAccount the newAccount to set
     */
    public void setNewAccount(boolean newAccount) {
        this.newAccount = newAccount;
    }

    public boolean isExistDataContractDigestion(ConnectionWrapper con) throws SQLException {
        if (this.getSlipNo() == null || this.getSlipNo() < 1) {
            return false;
        }

        if (con == null) {
            return false;
        }
        System.out.println("**************************:" + this.getExistDataContractDigestionSQL());
        ResultSetWrapper rs = con.executeQuery(this.getExistDataContractDigestionSQL());

        int count = 0;
        if (rs.next()) {
            count = rs.getInt("count");
        }
        System.out.println("****************************count:" + count);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

    private String getExistDataContractDigestionSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*)");
        sql.append(" from data_sales ds");
        sql.append("  inner join data_contract dc");
        sql.append("  on ds.shop_id = dc.shop_id and ds.slip_no = dc.slip_no");
        sql.append(" where ds.delete_date is null ");
        sql.append(" and dc.delete_date is null ");
        sql.append(" and ds.shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID()));
        sql.append(" and ds.slip_no = ").append(SQLUtil.convertForSQL(this.getSlipNo()));
        sql.append(" and exists");
        sql.append(" (");
        sql.append(" select * ");
        sql.append(" from data_sales ds1 ");
        sql.append("  inner join data_contract_digestion dcd ");
        //nhanvt start edit 20141117 Bug #32409
        sql.append("  on ds1.shop_id = dcd.contract_shop_id and ds1.slip_no = dcd.slip_no");
        //nhanvt end edit 20141117 Bug #32409
        sql.append(" where ds1.delete_date is null ");
        sql.append(" and dcd.delete_date is null ");
        sql.append(" and ds1.shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID()));
        //nhanvt start edit 20141117 Bug #32409
        sql.append(" and dcd.contract_shop_id = dc.shop_id and dcd.contract_no=dc.contract_no and dcd.contract_detail_no = dc.contract_detail_no");
        //nhanvt end edit 20141117 Bug #32409
//        sql.append(" and ds.sales_date < ds1.sales_date");
        //sales_dateは時分秒が「00:00:00」固定なので登録日で比較
        sql.append(" and ds.insert_date < ds1.insert_date");
        sql.append(" )");


//        sql.append(" select count(dcd) as count ");
//        sql.append(" from data_sales ds ");
//        sql.append("  inner join data_contract dc ");
//        sql.append("  on ds.shop_id = dc.shop_id and ds.slip_no = dc.slip_no ");
//        sql.append("  inner join data_contract_digestion dcd ");
//        sql.append("  on dc.shop_id = dcd.shop_id and dc.contract_no = dcd.contract_no and dc.contract_detail_no = dcd.contract_detail_no ");
//        sql.append(" where ds.delete_date is null ");
//        sql.append(" and dc.delete_date is null ");
//        sql.append(" and dcd.delete_date is null ");
//        sql.append(" and ds.shop_id = ").append(SQLUtil.convertForSQL(this.getShop().getShopID()));
//        sql.append(" and ds.slip_no = ").append(SQLUtil.convertForSQL(this.getSlipNo()));

        return sql.toString();
    }
}
