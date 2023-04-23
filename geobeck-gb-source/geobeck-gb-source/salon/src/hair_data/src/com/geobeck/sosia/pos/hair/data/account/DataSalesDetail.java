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
 * �`�[���׃f�[�^
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
     * �X��
     */
    protected MstShop shop = new MstShop();
    /**
     * �`�[No.
     */
    protected Integer slipNo = null;
    /**
     * �����
     */
    protected java.util.Date salesDate = null;
    /**
     * �`�[����No.
     */
    protected Integer slipDetailNo = null;
    /**
     * �敪�h�c
     */
    protected Integer productDivision = null;
    /**
     * ���i
     */
    protected Product product = new Product();
    /**
     * ����
     */
    protected Integer productNum = 0;
    /**
     * ���z
     */
    protected Long productValue = 0l;
    /**
     * ������
     */
    protected Double discountRate = 0d;
    /**
     * �������z
     */
    protected Long discountValue = 0l;
    /**
     * �w��
     */
    protected boolean designated = false;
    /**
     * �A�v���[�`
     */
    protected boolean approached = false;
    /**
     * �X�^�b�t
     */
    protected MstStaff staff = new MstStaff();
    /**
     * �����敪
     */
    protected Integer discountDivision = null;
    /**
     * �{�p��
     */
    protected MstBed bed = new MstBed();
    /**
     * ����ŗ�
     */
    protected Double taxRate = 0d;
    /**
     * ������
     */
    protected Double consumptionNum = 0d;
    /**
     * �R�[�X
     */
    protected Course course;
    /**
     * �R�[�X�_��i�R�[�X�_��̏ꍇ��1���㖾�ׂɑ΂�1�R�[�X�_��j
     */
    protected DataContract dataContract;
    /**
     * �����R�[�X
     */
    protected ConsumptionCourse consumptionCourse;
    /**
     * �ҏW�\�Ȗ��ׂ��ǂ���
     */
    protected boolean isEditable = true;
    /**
     * ���̌_��ԍ� �_��O�̃R�[�X�ɂ͉��Ɍ_��ԍ��𔭍s���Ă����B �_��O�ł��R�[�X�����̖��ׂ�ǉ����邱�Ƃ��ł��邽�߁A
     * �_��O�R�[�X�𖾍ׂ���폜�����Ƃ��ɃR�[�X�����̖��ׂ��폜�ł���悤�ɉ��_��ԍ��ŕR�t���Ă���
     */
    protected String tmpContractNo;
    // Start add 20130410 nakhoa
    /**
     * �X��ID
     */
    protected Integer contractShopId;
    // End add 20130410 nakhoa
    //IVS NNTUAN START 20131127
    protected boolean pickUp = false;
    //IVS NNTUAN END 20131127
    /**
     * �R���X�g���N�^
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
     * �R���X�g���N�^
     *
     * @param productDivision �敪�h�c
     * @param product ���i
     */
    public DataSalesDetail(Integer productDivision, Product product) {
        this.setProduct(productDivision, product);
    }

    /**
     * �R���X�g���N�^
     *
     * @param productDivision �敪�h�c
     * @param product ���i
     */
    public DataSalesDetail(Integer productDivision, Course course) {
        this.setCourse(productDivision, course);
    }

    /**
     * �R���X�g���N�^
     *
     * @param productDivision �敪�h�c
     * @param product ���i
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
     * �X�܂��擾����B
     *
     * @return �X��
     */
    public MstShop getShop() {
        return shop;
    }

    /**
     * �X�܂��Z�b�g����B
     *
     * @param shop �X��
     */
    public void setShop(MstShop shop) {
        this.shop = shop;
    }

    /**
     * �`�[No.���擾����B
     *
     * @return �`�[No.
     */
    public Integer getSlipNo() {
        return slipNo;
    }

    /**
     * �`�[No.���Z�b�g����B
     *
     * @param slipNo �`�[No.
     */
    public void setSlipNo(Integer slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * �`�[����No.���擾����B
     *
     * @return �`�[����No.
     */
    public Integer getSlipDetailNo() {
        return slipDetailNo;
    }

    /**
     * �`�[����No.���Z�b�g����B
     *
     * @param slipDetailNo �`�[����No.
     */
    public void setSlipDetailNo(Integer slipDetailNo) {
        this.slipDetailNo = slipDetailNo;
    }

    /**
     * ��������擾����B
     *
     * @return �����
     */
    public java.util.Date getSalesDate() {
        return salesDate;
    }

    /**
     * ��������Z�b�g����B
     *
     * @param salesDate �����
     */
    public void setSalesDate(java.util.Date salesDate) {
        this.salesDate = salesDate;
    }

    /**
     * �敪�h�c���擾����B
     *
     * @return �敪�h�c
     */
    public Integer getProductDivision() {
        return productDivision;
    }

    /**
     * �敪�h�c���Z�b�g����B
     *
     * @param productDivision �敪�h�c
     */
    public void setProductDivision(Integer productDivision) {
        this.productDivision = productDivision;
    }

    public String getProductDivisionName() {
        //Start edit 20131105 lvut ( �敪 for product_division(7,9))
        switch (this.productDivision) {
            case 0:
                return "����";
            case 1:
                return "�Z�p";
            case 2:
                return "���i";
            case 3:
                return "�Z�N";
            case 4:
                return "����";
            case 5:
                return "�_��";
            case 6:
                return "����";
            case 7:
                return "�ύX";
            case 8:
                return "���";
            case 9:
                return "�ύX";
            default:
                return "";
        }
        //End edit 20131105 lvut ( �敪 for product_division(7,9))
    }

    /**
     * ���i���擾����B
     *
     * @return ���i
     */
    public Product getProduct() {
        return product;
    }

    /**
     * ���i���Z�b�g����B
     *
     * @param product ���i
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
     * ���ʂ��擾����B
     *
     * @return ����
     */
    public Integer getProductNum() {
        return productNum;
    }

    /**
     * ���ʂ��Z�b�g����B
     *
     * @param productNum ����
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
     * ���z���擾����B
     *
     * @return ���z
     */
    public Long getProductValue() {
        return productValue;
    }

    /**
     * ���z���Z�b�g����B
     *
     * @param productValue ���z
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
            // IVS SANG START EDIT 20131105 ���v
            //if(productDivision == 5 ){
            if (productDivision == 5 || productDivision == 7 || productDivision == 8 || productDivision == 9) {
                // IVS SANG END EDIT 20131105 ���v
                //�R�[�X�_��̏ꍇ�͐��ʁi�����񐔁j���ς���Ă����z�͕ς��Ȃ�
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
     * ���������擾����B
     *
     * @return ������
     */
    public Double getDiscountRate() {
        return discountRate;
    }

    /**
     * ���������Z�b�g����B
     *
     * @param discountRate ������
     */
    public void setDiscountRate(Double discountRate) {
        if (discountRate == null) {
            discountRate = 0d;
        }

        this.discountRate = discountRate;
    }

    /**
     * �������z���擾����B
     *
     * @return �������z
     */
    public Long getDiscountValue() {
        return discountValue;
    }

    /**
     * �������z���Z�b�g����B
     *
     * @param discountValue �������z
     */
    public void setDiscountValue(Long discountValue) {
        if (discountValue == null) {
            discountValue = 0l;
        }

        this.discountValue = discountValue;
    }

    /**
     * �w���t���O���擾����
     *
     * @return designated �w�� true:�w�� false:�t���[
     */
    public boolean getDesignated() {
        return designated;
    }

    /**
     * �w���t���O���Z�b�g����
     *
     * @param designated �w���t���O
     */
    public void setDesignated(boolean designated) {
        this.designated = designated;
    }

    /**
     * �A�v���[�`�t���O���擾����
     *
     * @return approached �A�v���[�` true:���� false:�Ȃ�
     */
    public boolean getApproached() {
        return approached;
    }

    /**
     * �A�v���[�`�t���O���Z�b�g����
     *
     * @param approached �A�v���[�`�t���O
     */
    public void setApproached(boolean approached) {
        this.approached = approached;
    }

    /**
     * �X�^�b�t���擾����B
     *
     * @return �X�^�b�t
     */
    public MstStaff getStaff() {
        return staff;
    }

    /**
     * �X�^�b�t���Z�b�g����B
     *
     * @param staff �X�^�b�t
     */
    public void setStaff(MstStaff staff) {
        this.staff = staff;
    }

    /**
     * �����敪���擾����
     *
     * @return �����敪
     */
    public Integer getDiscountDivision() {
        return discountDivision;
    }

    /**
     * �����敪��ݒ肷��
     *
     * @param discountDivision �����敪
     */
    public void setDiscountDivision(Integer discountDivision) {
        this.discountDivision = discountDivision;
    }

    /**
     * �{�p����擾����B
     *
     * @return �{�p��
     */
    public MstBed getBed() {
        return bed;
    }

    /**
     * �{�p����Z�b�g����B
     *
     * @param bed �{�p��
     */
    public void setBed(MstBed bed) {
        this.bed = bed;
    }

    /**
     * ����ŗ����擾����B
     *
     * @return ����ŗ�
     */
    public Double getTaxRate() {
        return taxRate;
    }

    /**
     * ����ŗ����Z�b�g����B
     *
     * @param taxRate ����ŗ�
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
     * ���i���Z�b�g����B
     *
     * @param productDivision �敪�h�c
     * @param product ���i
     */
    public void setProduct(Integer productDivision, Product product) {
        this.setProductDivision(productDivision);
        this.setProduct(product);
        this.setProductNum(0);
        this.setProductValue(product.getPrice());
    }

    /**
     * �R�[�X���Z�b�g����B
     *
     * @param productDivision �敪�h�c
     * @param product ���i
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
     * �����R�[�X���Z�b�g����B
     *
     * @param productDivision �敪�h�c
     * @param product ���i
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
    //�S�̊��������l�Őō����犄���̏ꍇ�͏����_�ȉ��؂�̂ĂƂ��Ă��������B
    //��������[���Ƃ��Đݒ肵�Ă���������΁B
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

            // �S�̊���

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

            // ���׊���

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
     * ���v���擾����B
     *
     * @return ���v
     */
    public Long getTotal() {
        long value = 0;
        long discount = 0;
        // IVS SANG START INSERT 20131128 Bug #16623: [gb�\�[�X]�ڋq����ʁ˗��X���^�u�A��������EXCEL�o��
        if (productDivision == 5 || productDivision == 8) {
            value = productValue;
        } // IVS SANG END INSERT 20131128 Bug #16623: [gb�\�[�X]�ڋq����ʁ˗��X���^�u�A��������EXCEL�o�� 
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
     * ����ł��擾����B
     *
     * @return �����
     */
    public Long getTax(Double taxRate, Integer rounding) {
        return TaxUtil.getTax(this.getTotal(), taxRate, rounding);
    }

    /**
     * ResultSetWrapper����f�[�^���Z�b�g����B
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException ��O
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
     * ResultSetWrapper���犄���f�[�^���Z�b�g����B
     *
     * @param rs ResultSetWrapper
     * @throws java.sql.SQLException ��O
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
        //IVS_LVTu edit 2019/09/06  SPOS���őΉ�
        this.setTaxRate(rs.getDouble("tax_rate"));

        MstStaff ms = new MstStaff();
        ms.setStaffID(rs.getInt("staff_id"));
        ms.setStaffNo(rs.getString("staff_no"));
        ms.setStaffName(0, rs.getString("staff_name1"));
        ms.setStaffName(1, rs.getString("staff_name2"));
    }

    /**
     * �V�����`�[����No.���Z�b�g����B
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException ��O
     */
    public void setNewSlipDetailNo(ConnectionWrapper con) throws SQLException {
        ResultSetWrapper rs = con.executeQuery(this.getNewSlipDetailNoSQL());

        if (rs.next()) {
            this.setSlipDetailNo(rs.getInt("new_slip_detail_no"));
        }
        rs.close();
    }

    /**
     * �`�[���׃f�[�^��o�^����B
     *
     * @param con ConnectionWrapper
     * @return true - ����
     * @throws java.sql.SQLException ��O
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
     * �`�[���׃f�[�^�̑��݃`�F�b�N���s���B
     *
     * @param con ConnectionWrapper
     * @return true - ���݂���B
     * @throws java.sql.SQLException ��O
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
     * �V�����`�[����No.���擾����r�p�k�����擾����B
     *
     * @return �V�����`�[����No.���擾����r�p�k��
     */
    public String getNewSlipDetailNoSQL() {
        return "select coalesce(max(slip_detail_no), 0) + 1 as new_slip_detail_no\n"
                + "from data_sales_detail\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n";
    }

    /**
     * �`�[���׃f�[�^���擾����r�p�k�����擾����B
     *
     * @return �`�[���׃f�[�^���擾����r�p�k��
     */
    public String getSelectSQL() {
        return "select *\n"
                + "from data_sales_detail\n"
                + "where shop_id = " + SQLUtil.convertForSQL(this.getShop().getShopID()) + "\n"
                + "and slip_no = " + SQLUtil.convertForSQL(this.getSlipNo()) + "\n"
                + "and slip_detail_no = " + SQLUtil.convertForSQL(this.getSlipDetailNo()) + "\n";
    }

    /**
     * �`�[���׃f�[�^���擾����r�p�k�����擾����B
     *
     * @return �`�[���׃f�[�^���擾����r�p�k��
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
     * �`�[���׃f�[�^��Insert����r�p�k�����擾����B
     *
     * @return �`�[���׃f�[�^��Insert����r�p�k��
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
     * �`�[���׃f�[�^��Update����r�p�k�����擾����B
     *
     * @return �`�[���׃f�[�^��Update����r�p�k��
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
