/*
 * DataSalesDetail.java
 *
 * Created on 2006/05/26, 14:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.geobeck.sosia.pos.hair.data.account;

import com.geobeck.sosia.pos.hair.data.course.DataContract;
import com.geobeck.sosia.pos.hair.master.company.MstBed;
import com.geobeck.sosia.pos.products.*;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

import com.geobeck.sql.*;
import com.geobeck.util.*;
import com.geobeck.sosia.pos.util.*;
import com.geobeck.sosia.pos.master.account.*;
import com.geobeck.sosia.pos.master.company.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 伝票明細データ
 *
 * @author katagiri
 */
public class DataSalesDetail extends ArrayList<DataSalesProportionally> {

    public static final int PRODUCT_DIVISION_DISCOUNT = 0;
    public static final int PRODUCT_DIVISION_TECHNIC = 1;
    public static final int PRODUCT_DIVISION_ITEM = 2;
    private MstAccountSetting accountSetting = null;
    private Boolean applyAccountFilter = false;
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
     * 伝票明細No.
     */
    protected Integer slipDetailNo = null;
    /**
     * 区分ＩＤ
     */
    protected Integer productDivision = null;
    /**
     * 商品
     */
    protected Product product = new Product();
    /**
     * 数量
     */
    protected Integer productNum = 0;
    /**
     * 金額
     */
    protected Long productValue = 0l;
    /**
     * 割引率
     */
    protected Double discountRate = 0d;
    /**
     * 割引金額
     */
    protected Long discountValue = 0l;
    /**
     * 指名
     */
    protected boolean designated = false;
    /**
     * アプローチ
     */
    protected boolean approached = false;
    /**
     * スタッフ
     */
    protected MstStaff staff = new MstStaff();
    /**
     * 割引区分
     */
    protected Integer discountDivision = null;
    /**
     * 施術台
     */
    protected MstBed bed = new MstBed();
    /**
     * 消費税率
     */
    protected Double taxRate = 0d;
    /**
     * 消化回数
     */
    protected Double consumptionNum = 0d;
    /**
     * コース
     */
    protected Course course;
    /**
     * コース契約（コース契約の場合は1売上明細に対し1コース契約）
     */
    protected DataContract dataContract;
    /**
     * 消化コース
     */
    protected ConsumptionCourse consumptionCourse;
    /**
     * 編集可能な明細かどうか
     */
    protected boolean isEditable = true;
    /**
     * 仮の契約番号 契約前のコースには仮に契約番号を発行しておく。 契約前でもコース消化の明細を追加することができるため、
     * 契約前コースを明細から削除したときにコース消化の明細も削除できるように仮契約番号で紐付けておく
     */
    protected String tmpContractNo;
    // Start add 20130410 nakhoa
    /**
     * 店舗ID
     */
    protected Integer contractShopId;
    // End add 20130410 nakhoa
    //IVS NNTUAN START 20131127
    protected boolean pickUp = false;
    //IVS NNTUAN END 20131127
    /**
     * コンストラクタ
     */
    private Integer contractNo;
    private Integer contractDetailNo;
    
    protected boolean proposalFalg = false;

    public boolean isProposalFalg() {
        return proposalFalg;
    }

    public void setProposalFalg(boolean proposalFalg) {
        this.proposalFalg = proposalFalg;
    }

    public boolean isIsEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public Integer getContractNo() {
        return contractNo;
    }

    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

    public Integer getContractDetailNo() {
        return contractDetailNo;
    }

    public void setContractDetailNo(Integer contractDetailNo) {
        this.contractDetailNo = contractDetailNo;
    }

    public DataSalesDetail() {
    }

    /**
     * コンストラクタ
     *
     * @param productDivision 区分ＩＤ
     * @param product 商品
     */
    public DataSalesDetail(Integer productDivision, Product product) {
        this.setProduct(productDivision, product);
    }

    /**
     * コンストラクタ
     *
     * @param productDivision 区分ＩＤ
     * @param product 商品
     */
    public DataSalesDetail(Integer productDivision, Course course) {
        this.setCourse(productDivision, course);
    }

    /**
     * コンストラクタ
     *
     * @param productDivision 区分ＩＤ
     * @param product 商品
     */
    public DataSalesDetail(Integer productDivision, ConsumptionCourse consumptionCourse) {
        this.setConsumptionCourse(productDivision, consumptionCourse);
    }

    public MstAccountSetting getAccountSetting() {
        return accountSetting;
    }

    public void setAccountSetting(MstAccountSetting accountSetting) {
        this.accountSetting = accountSetting;
    }

    public Boolean isApplyAccountFilter() {
        return applyAccountFilter;
    }

    public void setApplyAccountFilter(Boolean accountFilter) {
        this.applyAccountFilter = applyAccountFilter;
    }

    public boolean getPickUp() {
        return pickUp;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
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
     * 伝票明細No.を取得する。
     *
     * @return 伝票明細No.
     */
    public Integer getSlipDetailNo() {
        return slipDetailNo;
    }

    /**
     * 伝票明細No.をセットする。
     *
     * @param slipDetailNo 伝票明細No.
     */
    public void setSlipDetailNo(Integer slipDetailNo) {
        this.slipDetailNo = slipDetailNo;
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
     * 区分ＩＤを取得する。
     *
     * @return 区分ＩＤ
     */
    public Integer getProductDivision() {
        return productDivision;
    }

    /**
     * 区分ＩＤをセットする。
     *
     * @param productDivision 区分ＩＤ
     */
    public void setProductDivision(Integer productDivision) {
        this.productDivision = productDivision;
    }

    public String getProductDivisionName() {
        //Start edit 20131105 lvut ( 区分 for product_division(7,9))
        switch (this.productDivision) {
            case 0:
                return "割引";
            case 1:
                return "技術";
            case 2:
                return "商品";
            case 3:
                return "技ク";
            case 4:
                return "商返";
            case 5:
                return "契約";
            case 6:
                return "消化";
            case 7:
                return "変更";
            case 8:
                return "解約";
            case 9:
                return "変更";
            default:
                return "";
        }
        //End edit 20131105 lvut ( 区分 for product_division(7,9))
    }

    /**
     * 商品を取得する。
     *
     * @return 商品
     */
    public Product getProduct() {
        return product;
    }

    /**
     * 商品をセットする。
     *
     * @param product 商品
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    public String getProductName() {
        if (this.getProduct() != null) {
            return this.getProduct().getProductName();
        }

        return "";
    }

    /**
     * 数量を取得する。
     *
     * @return 数量
     */
    public Integer getProductNum() {
        return productNum;
    }

    /**
     * 数量をセットする。
     *
     * @param productNum 数量
     */
    public void setProductNum(Integer productNum) {
        if (productNum == null) {
            productNum = 0;
        }

        this.productNum = productNum;
        // vtbphuong start add 20140626 Bug #26082
        this.resetDiscount();
        // vtbphuong end add 20140626 Bug #26082
    }

    /**
     * 金額を取得する。
     *
     * @return 金額
     */
    public Long getProductValue() {
        return productValue;
    }

    /**
     * 金額をセットする。
     *
     * @param productValue 金額
     */
    public void setProductValue(Long productValue) {
        if (productValue == null) {
            productValue = 0l;
        }

        this.productValue = productValue;

        this.resetDiscount();
    }

    public Long getValue() {
        long value = 0;

        if (productNum != null && productValue != null) {
            // IVS SANG START EDIT 20131105 合計
            //if(productDivision == 5 ){
            if (productDivision == 5 || productDivision == 7 || productDivision == 8 || productDivision == 9) {
                // IVS SANG END EDIT 20131105 合計
                //コース契約の場合は数量（消化回数）が変わっても金額は変わらない
                value = productValue;
            } else {
                value = productValue * productNum;
            }
        }

        return value;
    }

    public Double getValueForConsumption() {
        double value = 0d;

        if (productNum != null && productValue != null) {
            if (productDivision == 6) {
                value = productValue * consumptionNum;
            }
        }

        return value;
    }

    public void setAllDiscount(Double discount, Integer discountMethod) {
        if (discount == null) {
            discount = 0d;
        }

        if (discountMethod != null && discountMethod.equals(MstDiscount.DISCOUNT_METHOD_RATE)) {
            this.setDiscountRate(discount);
            this.resetDiscount();
        } else {
            this.setDiscountRate(0d);
            this.setDiscountValue(discount.longValue());
        }
    }

    public void setDiscount(Double discount) {
        if (discount == null) {
            discount = 0d;
        }

        if (0 < Math.abs(discount) && Math.abs(discount) < 1) {
            this.setDiscountRate(discount);
            this.resetDiscount();
        } else {
            this.setDiscountRate(0d);
            this.setDiscountValue(discount.longValue());
        }
    }

    public Double getDiscount() {
        if (0d < this.getDiscountRate()) {
            return this.getDiscountRate();
        } else {
            return this.getDiscountValue().doubleValue();
        }
    }

    /**
     * 割引率を取得する。
     *
     * @return 割引率
     */
    public Double getDiscountRate() {
        return discountRate;
    }

    /**
     * 割引率をセットする。
     *
     * @param discountRate 割引率
     */
    public void setDiscountRate(Double discountRate) {
        if (discountRate == null) {
            discountRate = 0d;
        }

        this.discountRate = discountRate;
    }

    /**
     * 割引金額を取得する。
     *
     * @return 割引金額
     */
    public Long getDiscountValue() {
        return discountValue;
    }

    /**
     * 割引金額をセットする。
     *
     * @param discountValue 割引金額
     */
    public void setDiscountValue(Long discountValue) {
        if (discountValue == null) {
            discountValue = 0l;
        }

        this.discountValue = discountValue;
    }

    /**
     * 指名フラグを取得する
     *
     * @return designated 指名 true:指名 false:フリー
     */
    public boolean getDesignated() {
        return designated;
    }

    /**
     * 指名フラグをセットする
     *
     * @param designated 指名フラグ
     */
    public void setDesignated(boolean designated) {
        this.designated = designated;
    }

    /**
     * アプローチフラグを取得する
     *
     * @return approached アプローチ true:あり false:なし
     */
    public boolean getApproached() {
        return approached;
    }

    /**
     * アプローチフラグをセットする
     *
     * @param approached アプローチフラグ
     */
    public void setApproached(boolean approached) {
        this.approached = approached;
    }

    /**
     * スタッフを取得する。
     *
     * @return スタッフ
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * スタッフをセットする。
     *
     * @param staff スタッフ
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * 割引区分を取得する
     *
     * @return 割引区分
     */
    public Integer getDiscountDivision() {
        return discountDivision;
    }

    /**
     * 割引区分を設定する
     *
     * @param discountDivision 割引区分
     */
    public void setDiscountDivision(Integer discountDivision) {
        this.discountDivision = discountDivision;
    }

    /**
     * 施術台を取得する。
     *
     * @return 施術台
     */
    public MstBed getBed() {
        return bed;
    }

    /**
     * 施術台をセットする。
     *
     * @param bed 施術台
     */
    public void setBed(MstBed bed) {
        this.bed = bed;
    }

    /**
     * 消費税率を取得する。
     *
     * @return 消費税率
     */
    public Double getTaxRate() {
        return taxRate;
    }

    /**
     * 消費税率をセットする。
     *
     * @param taxRate 消費税率
     */
    public void setTaxRate(Double taxRate) {
        if (taxRate == null) {
            taxRate = 0d;
        }

        this.taxRate = taxRate;
    }

    public Double getConsumptionNum() {
        return consumptionNum;
    }

    public void setConsumptionNum(Double consumptionNum) {
        this.consumptionNum = consumptionNum;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public DataContract getDataContract() {
        return dataContract;
    }

    public void setDataContract(DataContract dataContract) {
        this.dataContract = dataContract;
    }

    public ConsumptionCourse getConsumptionCourse() {
        return this.consumptionCourse;
    }

    public void setConsumptionCourse(ConsumptionCourse consumptionCourse) {
        this.consumptionCourse = consumptionCourse;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
    }

    public String getTmpContractNo() {
        return tmpContractNo;
    }

    public void setTmpContractNo(String tmpContractNo) {
        this.tmpContractNo = tmpContractNo;
    }

    // Start add 20130410 nakhoa
    public Integer getContractShopId() {
        return contractShopId;
    }

    public void setContractShopId(Integer contractShopId) {
        this.contractShopId = contractShopId;
    }
    // End add 20130410 nakhoa

    /**
     * 商品をセットする。
     *
     * @param productDivision 区分ＩＤ
     * @param product 商品
     */
    public void setProduct(Integer productDivision, Product product) {
        this.setProductDivision(productDivision);
        this.setProduct(product);
        this.setProductNum(0);
        this.setProductValue(product.getPrice());
    }

    /**
     * コースをセットする。
     *
     * @param productDivision 区分ＩＤ
     * @param product 商品
     */
    public void setCourse(Integer productDivision, Course course) {
        this.setProductDivision(productDivision);
        this.setCourse(course);
        Product p = new Product();
        p.setProductID(course.getCourseId());
        this.setProduct(p);
        // vtbphuong start delete 20150605 
       // this.setProductNum(0);
        // vtbphuong start delete 20150605 
        this.setProductValue(course.getPrice());
    }

    /**
     * 消化コースをセットする。
     *
     * @param productDivision 区分ＩＤ
     * @param product 商品
     */
    public void setConsumptionCourse(Integer productDivision, ConsumptionCourse consumptionCourse) {
        this.setProductDivision(productDivision);
        this.setConsumptionCourse(consumptionCourse);
        this.setConsumptionNum(0d);
        Product p = new Product();
        p.setProductID(consumptionCourse.getCourseId());
        this.setProduct(p);
        this.setProductNum(0);
        this.setProductValue(consumptionCourse.getPrice());
        // Start add 20130410 nakhoa
        this.setContractShopId(consumptionCourse.getContractShopId());
        // End add 20130410 nakhoa
    }
    //IVS_LVTu start edit 2015/07/15 Delete comment code.
    //全体割引も同様で税込から割引の場合は小数点以下切り捨てとしてください。
    //これもルールとして設定していただければ。
    public void resetDiscount() {
          if(this.getAccountSetting()==null) {
              try {
              MstAccountSetting ms = new MstAccountSetting();
              ms.load(SystemInfo.getConnection());
             this.setAccountSetting(ms); 
              }catch(SQLException e) {
                  e.printStackTrace();
              }
          }
        if (this.getProductDivision().equals(0)) {

            // 全体割引

            if (0 < Math.abs(discountRate)) {

                    if (this.getAccountSetting().getDiscountType().equals(1)) {
                        Double taxRate = this.getAccountSetting().getTaxRate() ; //SystemInfo.getTaxRate(SystemInfo.getSystemDate());
                        Long temp = this.getAccountSetting().round(this.getValue().doubleValue());
                        this.setDiscountValue(((Double) (temp * discountRate)).longValue());
                    } else {
                        this.setDiscountValue(((Double) (this.getValue().doubleValue() * discountRate)).longValue());
                    }
            }

        } else {

            // 明細割引

            if (0 < Math.abs(discountRate) && Math.abs(discountRate) < 1) {

                if (this.getAccountSetting().getDiscountType().equals(1)) {
                    //Double taxRate = this.getAccountSetting().getTaxRate();//; SystemInfo.getTaxRate(SystemInfo.getSystemDate());
                    Double taxRate = this.getTaxRate();
                    if (taxRate == null) {
                        taxRate = 0d;
                    }
                    BigDecimal a = new BigDecimal(this.getValue().doubleValue() / (1 + taxRate));
                    a = a.setScale(3, RoundingMode.HALF_UP);
                    Long taxOffValue = ((Double) Math.ceil(a.doubleValue())).longValue();
                    this.setDiscountValue(this.getAccountSetting().round(taxOffValue * discountRate));
                } else {
                    //TVHoa start edit 20150730 Bug #41177
                    this.setDiscountValue(((Double) (Math.floor(this.getValue().doubleValue() * (discountRate * 100) / 100))).longValue());
                    //TVHoa end edit 20150730 Bug #41177
                }
            }
        }

    }
    //IVS_LVTu end edit 2015/07/15 Delete comment code.

    /**
     * 合計を取得する。
     *
     * @return 合計
     */
    public Long getTotal() {
        long value = 0;
        long discount = 0;
        // IVS SANG START INSERT 20131128 Bug #16623: [gbソース]顧客情報画面⇒来店情報タブ、条件検索EXCEL出力
        if (productDivision == 5 || productDivision == 8) {
            value = productValue;
        } // IVS SANG END INSERT 20131128 Bug #16623: [gbソース]顧客情報画面⇒来店情報タブ、条件検索EXCEL出力 
        else if (productNum != null && productValue != null) {
            value = productValue * productNum;
        }

        if (discountValue != null) {
            discount = discountValue;
        }
        //nhanvt start edit 20150206 Bug #35176
        Double taxRate = 0d;
        if(this.getSalesDate() != null){
            taxRate = SystemInfo.getTaxRate(this.getSalesDate());
        }else {
            taxRate = SystemInfo.getTaxRate(Calendar.getInstance().getTime());
        }
        return SystemInfo.getAccountSetting().getDisplayValue(value, discount, taxRate);
        //nhanvt end edit 20150206 Bug #35176
    }

    /**
     * 消費税を取得する。
     *
     * @return 消費税
     */
    public Long getTax(Double taxRate, Integer rounding) {
        return TaxUtil.getTax(this.getTotal(), taxRate, rounding);
    }

    /**
     * ResultSetWrapperからデータをセットする。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException 例外
     */
    //Luc start add 20150225 Bug #35220
    public void loadForConsumption (Integer shopId, Integer slipNo,Integer contractNo,Integer contractDetailNo,Integer contractShopId) {
        StringBuffer sql = new StringBuffer();
                
        sql.append(" Select dsd.shop_id,dsd.slip_no,dsd.slip_detail_no,dsd.contract_no,dsd.contract_detail_no,dsd.contract_shop_id,dsd.designated_flag,dsd.approached_flag,");
        sql.append(" ms.staff_id,ms.staff_no,ms.staff_name1,ms.staff_name2");
        sql.append(" from data_sales_detail dsd left join mst_staff ms on dsd.staff_id = ms.staff_id");
        sql.append("  where dsd.shop_id = "+shopId+" And dsd.slip_no="+slipNo) ;
        sql.append("  And dsd.contract_no ="+contractNo +" And dsd.contract_detail_no="+contractDetailNo + " And dsd.contract_shop_id="+contractShopId);
       try {
            ResultSetWrapper rs = SystemInfo.getConnection().executeQuery(sql.toString());
            rs.next();
            setDataForConsumption(rs);
       }catch (SQLException e) {
          System.out.println(e.getMessage());
       }
    }
    public void setDataForConsumption(ResultSetWrapper rs) throws SQLException {
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));        
        this.setDesignated(rs.getBoolean("designated_flag"));
        this.setApproached(rs.getBoolean("approached_flag"));
        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        ms.setStaffNo(rs.getString("staff_no"));
        ms.setStaffName(0, rs.getString("staff_name1"));
        ms.setStaffName(1, rs.getString("staff_name2"));
        this.setStaff(ms);
    }
    //Luc end add 20150225 Bug #35220
    public void setData(ResultSetWrapper rs) throws SQLException {
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSalesDate(rs.getDate("sales_date"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));

        ProductClass pc = new ProductClass();
        Product p = new Product();
        //add by ltthuc 2014/06/13 

        if ((SystemInfo.getDatabase().equals("pos_hair_missionf") || SystemInfo.getDatabase().equals("pos_hair_missionf_dev")) && SystemInfo.getSetteing().isUsePrepaid()) {
            p.setPrepa_id(rs.getInt("prepa_id"));
            p.setPrepaid_price(rs.getLong("prepaid_price"));
            pc.setPrepa_class_id(rs.getInt("prepa_class_id"));
        }
        // end add by ltthuc
        pc.setProductClassID(rs.getInt("product_class_id"));
        pc.setProductClassName(rs.getString("product_class_name"));
        //Luc start add 20150817 #41948
        pc.setShopCategoryID(rs.getInt("shop_category_id"));
        //Luc start add 20150817 #41948
        p.setProductClass(pc);
        p.setProductID(rs.getInt("product_id"));
        p.setProductNo(rs.getString("product_no"));
        p.setProductName(rs.getString("product_name"));
        p.setPraiseTimeLimit(rs.getInt("praise_time_limit"));
        p.setPraiseTime(rs.getBoolean("is_praise_time"));

        // IVS SANG START INSERT 20131011
        try {
            p.setOperationTime(rs.getInt("operation_time"));
        } catch (Exception e) {
        }
        // IVS SANG END INSERT 20131011
        this.setTaxRate(rs.getDouble("tax_rate"));
        this.setProduct(rs.getInt("product_division"), p); 
        this.setDiscountRate(rs.getDouble("discount_rate"));
        this.setDiscountValue(rs.getLong("discount_value"));
        this.setProductNum(rs.getInt("product_num"));
        this.setProductValue(rs.getLong("product_value"));
        //this.setDiscountRate(rs.getDouble("discount_rate"));
        //this.setDiscountValue(rs.getLong("discount_value"));
        this.setDesignated(rs.getBoolean("designated_flag"));
        this.setApproached(rs.getBoolean("approached_flag"));
        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        ms.setStaffNo(rs.getString("staff_no"));
        ms.setStaffName(0, rs.getString("staff_name1"));
        ms.setStaffName(1, rs.getString("staff_name2"));
        this.setStaff(ms);
    }

    /**
     * ResultSetWrapperから割引データをセットする。
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException 例外
     */
    public void setDiscountData(ResultSetWrapper rs) throws SQLException {
        this.getShop().setShopID(rs.getInt("shop_id"));
        this.setSlipNo(rs.getInt("slip_no"));
        this.setSlipDetailNo(rs.getInt("slip_detail_no"));
        this.setProductDivision(rs.getInt("product_division"));

        Product p = new Product();
        p.setProductID(rs.getInt("product_id"));
        p.setProductNo(rs.getString("product_no"));
        p.setProductName(rs.getString("discount_name"));
        this.setProduct(p);
        this.setProductValue(0l);
        this.setProductNum(1);
        this.setDiscountRate(rs.getDouble("discount_rate"));
        this.setDiscountValue(rs.getLong("discount_value"));
        this.setDiscountDivision(rs.getInt("discount_division"));
        //IVS_LVTu edit 2019/09/06  SPOS増税対応
        this.setTaxRate(rs.getDouble("tax_rate"));

        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        ms.setStaffNo(rs.getString("staff_no"));
        ms.setStaffName(0, rs.getString("staff_name1"));
        ms.setStaffName(1, rs.getString("staff_name2"));
    }

    /**
     * 新しい伝票明細No.をセットする。
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException 例外
     */
    public void setNewSlipDetailNo(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getNewSlipDetailNoSQL());

        if (rs.next()) {
            this.setSlipDetailNo(rs.getInt("new_slip_detail_no"));
        }
        rs.close();
    }

    /**
     * 伝票明細データを登録する。
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
        
        if(con.executeUpdate(sql) == 1){
            return true;
        }else {
            return false;
        }

    }

    /**
     * 伝票明細データの存在チェックを行う。
     *
     * @param con ConnectionWrapper
     * @return true - 存在する。
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
     * 新しい伝票明細No.を取得するＳＱＬ文を取得する。
     *
     * @return 新しい伝票明細No.を取得するＳＱＬ文
     */
    public String getNewSlipDetailNoSQL() {
        return "select coalesce(max(slip_detail_no), 0) + 1 as new_slip_detail_no\n"
                + "from data_sales_detail\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * 伝票明細データを取得するＳＱＬ文を取得する。
     *
     * @return 伝票明細データを取得するＳＱＬ文
     */
    public String getSelectSQL() {
        return "select *\n"
                + "from data_sales_detail\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n";
    }

    /**
     * 伝票明細データを取得するＳＱＬ文を取得する。
     *
     * @return 伝票明細データを取得するＳＱＬ文
     */
    public String getSelectDetailSQL() {
        return "select dsd.*,\n"
                + "case dsd.product_division\n"
                + "when 1 then mt.technic_class_id\n"
                + "when 2 then mi.item_class_id\n"
                + "else null end as product_class_id,\n"
                + "case dsd.product_division\n"
                + "when 1 then mtc.technic_class_name\n"
                + "when 2 then mic.item_class_name\n"
                + "else null end as product_class_name,\n"
                + "case dsd.product_division\n"
                + "when 1 then mt.technic_no\n"
                + "when 2 then mi.item_no\n"
                + "else null end as product_no,\n"
                + "case dsd.product_division\n"
                + "when 1 then mt.technic_name\n"
                + "when 2 then mi.item_name\n"
                + "else null end as product_name,\n"
                + "dsd.product_num,\n"
                + "dsd.product_value,\n"
                + "dsd.discount_rate,\n"
                + "dsd.discount_value,\n"
                + "dsd.designated_flag,\n"
                + "dsd.approached_flag,\n"
                + "dsd.staff_id,\n"
                + "ms.staff_name1,\n"
                + "ms.staff_name2\n"
                + "from data_sales_detail dsd\n"
                + "left outer join mst_technic mt\n"
                + "on mt.technic_id = dsd.product_id\n"
                + "left outer join mst_technic_class mtc\n"
                + "on mtc.technic_class_id = mt.technic_class_id\n"
                + "left outer join mst_item mi\n"
                + "on mi.item_id = dsd.product_id\n"
                + "left outer join mst_item_class mic\n"
                + "on mic.item_class_id = mi.item_class_id\n"
                + "left outer join mst_staff ms\n"
                + "on ms.staff_id = dsd.staff_id\n"
                + "where dsd.delete_date is null\n"
                + "and dsd.shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and dsd.slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and dsd.slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n"
                + "and dsd.product_division != 0\n";
    }

    /**
     * 伝票明細データをInsertするＳＱＬ文を取得する。
     *
     * @return 伝票明細データをInsertするＳＱＬ文
     */
    public String getInsertSQL() {
        return "insert into data_sales_detail\n"
                + "(shop_id, slip_no, slip_detail_no, product_division, product_id,\n"
                + "product_num, product_value, discount_rate, discount_value,\n"
                + "designated_flag, approached_flag, staff_id,contract_shop_id,contract_no,contract_detail_no,\n"
                + "tax_rate,\n"
                + "insert_date, update_date, delete_date)\n"
                + "values(\n"
                + SQLUtil.convertForSQL(this.getShop().getShopID()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getSlipDetailNo()) + ",\n"
                + SQLUtil.convertForSQL(this.getProductDivision()) + ",\n"
                + SQLUtil.convertForSQL(this.getProduct().getProductID()) + ",\n"
                + SQLUtil.convertForSQL(this.getProductNum(), "0") + ",\n"
                + SQLUtil.convertForSQL(this.getProductValue(), "0") + ",\n"
                + SQLUtil.convertForSQL(this.getDiscountRate(), "0") + ",\n"
                + SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n"
                + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + SQLUtil.convertForSQL(this.getApproached()) + ",\n"
                + SQLUtil.convertForSQL((this.getStaff().getStaffID() == null
                || this.getStaff().getStaffID() <= 0 ? null
                : this.getStaff().getStaffID())) + ",\n"
                + SQLUtil.convertForSQL(this.getContractShopId(), "null") + ",\n"
                + SQLUtil.convertForSQL(this.getContractNo(), "null") + ",\n"
                + SQLUtil.convertForSQL(this.getContractDetailNo(), "null") + ",\n"
                + SQLUtil.convertForSQL(this.getTaxRate(), "null") + ",\n"
                + "current_timestamp, current_timestamp, null)\n";
    }

    /**
     * 伝票明細データをUpdateするＳＱＬ文を取得する。
     *
     * @return 伝票明細データをUpdateするＳＱＬ文
     */
    public String getUpdateSQL() {
        return "update data_sales_detail\n"
                + "set\n"
                + "product_division = " + SQLUtil.convertForSQL(this.getProductDivision()) + ",\n"
                + "product_id = " + SQLUtil.convertForSQL(this.getProduct().getProductID()) + ",\n"
                + "product_num = " + SQLUtil.convertForSQL(this.getProductNum(), "0") + ",\n"
                + "product_value = " + SQLUtil.convertForSQL(this.getProductValue(), "0") + ",\n"
                + "discount_rate = " + SQLUtil.convertForSQL(this.getDiscountRate(), "0") + ",\n"
                + "discount_value = " + SQLUtil.convertForSQL(this.getDiscountValue(), "0") + ",\n"
                + "designated_flag = " + SQLUtil.convertForSQL(this.getDesignated()) + ",\n"
                + "approached_flag = " + SQLUtil.convertForSQL(this.getApproached()) + ",\n"
                + "contract_shop_id = " + SQLUtil.convertForSQL(this.getContractShopId(), "null") + ",\n"
                + "contract_no = " + SQLUtil.convertForSQL(this.getContractNo(), "null") + ",\n"
                + "contract_detail_no = " + SQLUtil.convertForSQL(this.getContractDetailNo(), "null") + ",\n"
                + "staff_id = " + SQLUtil.convertForSQL((this.getStaff().getStaffID()==null ? null
                : this.getStaff().getStaffID())) + ",\n"
                + "tax_rate = " + SQLUtil.convertForSQL(this.getTaxRate(), "null") + ",\n"
                + "update_date = current_timestamp\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo());
    }
}
