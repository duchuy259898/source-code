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
 * �`�[���͏����i�����N�[�[�V�����p�j
 *
 * @author katagiri
 */
public class HairInputAccount {

    /**
     * ��v�ݒ�}�X�^
     */
    protected MstAccountSetting accountSetting = new MstAccountSetting();
    /**
     * �����}�X�^
     */
    protected MstDiscounts discounts = new MstDiscounts();
    /**
     * �x���敪
     */
    protected MstPaymentClasses paymentClasses = new MstPaymentClasses();
    /**
     * ���v
     */
    protected ArrayList<NameValue> total = new ArrayList<NameValue>();
    /**
     * ���|���f�[�^
     */
    protected DataPayment bill = new DataPayment();
    /**
     * �X�^�b�t���X�g
     */
    protected MstStaffs staffs = null;
    
    // IVS_Thanh start add 2014/07/07 GB_Mashu_�ƑԎ�S���ݒ�
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
    // IVS_Thanh end add 2014/07/07 GB_Mashu_�ƑԎ�S���ݒ�
    
    /**
     * �`�[�w�b�_�f�[�^
     */
    protected DataSales hairSales = new DataSales(SystemInfo.getTypeID());
    public Integer reservationNo = null;
    private Double taxRate = 0d;
    /**
     * �ڋq��� 1�F�\���� 2�F�ݓX��� 3�F�ޓX���
     */
    private int status = 2;
    /**
     * �ڋq����� 1�F�{�p�҂� 2�F�{�p��
     */
    private int subStatus = 1;
    private boolean skipSales = false;
    /**
     * ��
     */
    private ArrayList<DataProportionally> proportionallys = new ArrayList<DataProportionally>();
    /**
     * ���X�|���X�f�[�^
     */
    protected DataResponseEffect[] dre = new DataResponseEffect[2];
    //�R�[�X�������v�z
    private long cunsumptionTotal = 0;

    //�{�p���Ԃ̕ύX start
    /** �{�p���ԁi���j */
    protected String opeMinute = null;
    /** �{�p���ԁi�b�j */
    protected String opeSecond = null;
    //�{�p���Ԃ̕ύX end
    
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
    
    // IVS_Thanh start add 2014/07/07 GB_Mashu_�ƑԎ�S���ݒ�
    public ArrayList<DataReservationMainstaff> getReservationMainstaffs() {
        return reservationMainstaffs;
    }

    public void setReservationMainstaffs(ArrayList<DataReservationMainstaff> reservationMainstaffs) {
        this.reservationMainstaffs = reservationMainstaffs;
    }
    // IVS_Thanh end add 2014/07/07 GB_Mashu_�ƑԎ�S���ݒ�

    //�{�p���Ԃ̕ύX start
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
    //�{�p���Ԃ̕ύX end
    
    /**
     * �`�[�w�b�_�f�[�^���擾����B
     *
     * @return �`�[�w�b�_�f�[�^
     */
    public DataSales getSales() {
        return hairSales;
    }

    /**
     * �`�[�w�b�_�f�[�^���Z�b�g����B
     *
     * @param sales �`�[�w�b�_�f�[�^
     */
    public void setSales(DataSales sales) {
        this.hairSales = hairSales;
    }

    /**
     * �����}�X�^���擾����B
     *
     * @return �����}�X�^
     */
    public MstDiscounts getDiscounts() {
        return discounts;
    }

    /**
     * �����}�X�^���Z�b�g����B
     *
     * @param discounts �����}�X�^
     */
    public void setDiscounts(MstDiscounts discounts) {
        this.discounts = discounts;
    }

    /**
     * �����}�X�^���Z�b�g����B
     *
     * @param discounts �����}�X�^
     */
    public void setReservationNo(Integer reservationNo) {
        this.reservationNo = reservationNo;
    }

    /**
     * �x���敪���擾����B
     *
     * @return �x���敪
     */
    public MstPaymentClasses getPaymentClasses() {
        return paymentClasses;
    }

    /**
     * �x���敪���Z�b�g����B
     *
     * @param paymentClasses �x���敪
     */
    public void setPaymentClasses(MstPaymentClasses paymentClasses) {
        this.paymentClasses = paymentClasses;
    }

    /**
     * ���v���擾����B
     *
     * @return ���v
     */
    public ArrayList<NameValue> getTotal() {
        return total;
    }

    /**
     * ���v���擾����B
     *
     * @param index �C���f�b�N�X
     * @return ���v
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
     * ���|�����擾����B
     *
     * @return ���|��
     */
    public DataPayment getBill() {
        return bill;
    }

    /**
     * ���|�����Z�b�g����B
     *
     * @param bill ���|��
     */
    public void setBill(DataPayment bill) {
        this.bill = bill;
    }

    /**
     * ���X�|���X�f�[�^���擾����B
     *
     * @return ���X�|���X�f�[�^
     */
    public DataResponseEffect[] getResponseEffect() {
        return dre;
    }

    /**
     * ���X�|���X�f�[�^���Z�b�g����B
     *
     * @param dre ���X�|���X�f�[�^
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
     * �������������s���B
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

        // �����X�g���擾����
        this.getProportionallys();
    }

    /**
     * �����X�g���擾����
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
     * �����X�g�擾SQL���擾����
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
     * ���v�̏������������s���B
     */
    protected void initTotal() {
        total.clear();
        total.add(new NameValue("���v", 0l));
        total.add(new NameValue("���׊���", 0l));
        total.add(new NameValue("�S�̊���", 0l));
        total.add(new NameValue("�������z", 0l));
        total.add(new NameValue("�i���Łj", 0l));
        total.add(new NameValue("���ނ�", 0l));
    }

    /**
     * �`�[���׃f�[�^��ǉ�����B
     *
     * @param productDivision �`�[���׋敪
     * @param product �`�[���׃f�[�^
     */
    public void addSalesDetail(Integer productDivision, Product product, MstStaff ms, boolean designated) {
        DataSalesDetail dsd = new DataSalesDetail(productDivision, product);
        dsd.setProductNum(1);
        dsd.setStaff(ms);
        dsd.setDesignated(designated);
        
        //IVS_LVTu start edit 2015/10/07 Bug #43006
        // �`�[���׋敪���Z�p���A���L�̓X�܂̏ꍇ�ɂ͈����ڂ�ǉ�����
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
     * �`�[���׃f�[�^��ǉ�����B
     *
     * @param productDivision �`�[���׋敪
     * @param product �`�[���׃f�[�^
     */
    public void addSalesDetail(Integer productDivision, Course course, MstStaff ms, boolean designated, String tmpContractNo) {
        DataSalesDetail dsd = new DataSalesDetail(productDivision, course);
        dsd.setProductNum(1);
        dsd.setStaff(ms);
        dsd.setDesignated(designated);
        dsd.setTmpContractNo(tmpContractNo);

//		// �`�[���׋敪���Z�p���A���L�̓X�܂̏ꍇ�ɂ͈����ڂ�ǉ�����
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
     * �`�[���׃f�[�^��ǉ�����B
     *
     * @param productDivision �`�[���׋敪
     * @param product �`�[���׃f�[�^
     */
    public void addSalesDetail(Integer productDivision, ConsumptionCourse consumptionCourse, MstStaff ms, boolean designated, String tmpContractNo) {
        DataSalesDetail dsd = new DataSalesDetail(productDivision, consumptionCourse);
        dsd.setConsumptionNum(1d);
        dsd.setStaff(ms);
        dsd.setDesignated(designated);
        dsd.setTmpContractNo(tmpContractNo);

//		// �`�[���׋敪���Z�p���A���L�̓X�܂̏ꍇ�ɂ͈����ڂ�ǉ�����
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
     * �`�[���׃f�[�^��ǉ�����B
     *
     * @param productDivision �`�[���׋敪
     * @param product �`�[���׃f�[�^
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
     * ���v���Z�b�g����B
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
//        //���i���v
//        this.getTotal(0).setValue(valueTotal+hairSales.getDetailDiscountTotal());
//        //�������v
//        this.getTotal(1).setValue(hairSales.getDetailDiscountTotal());
//       //-----------------------------------------
//        //�S�̊���
//        //-----------------------------------------
//        if(totalDiscountForTax>0){
//            taxTotal += this.getAccountSetting().getTax(totalValueForTax, totalDiscountForTax, this.getTaxRate());
//        }else {
//            //taxTotal += hairSales.getValueTotal()-valueTotal;
//             taxTotal += this.getAccountSetting().getTax(totalValueForTax, 0, this.getTaxRate());
//        }
//        //�S�̊����Ɋ܂܂�Ă������ŕ�������
//        taxTotal -= (this.getAccountSetting().getTax(this.getTotal(2).getValue(), 0l, this.getTaxRate()));
//        // vtbphuong start change 20140603 Bug #24589
////        if (taxTotal < 0) {
////            this.getTotal(4).setValue(0l);
////        } else {
////            this.getTotal(4).setValue(taxTotal);
////        }
//          this.getTotal(4).setValue(taxTotal);
//        // vtbphuong end change 20140603 Bug #24589
//        //�������z
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
//        //���ނ�
//        this.getTotal(5).setValue(temp);
//
//        //���|�����Z�b�g
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
		//���i���v
                // vtbphuong start change 20140606 Bug #24803
		//this.getTotal(0).setValue(this.taxFilter(hairSales.getValueTotal()));
                this.getTotal(0).setValue(this.taxFilter());
                // vtbphuong start change 20140606 Bug #24803
		//�������v
		this.getTotal(1).setValue(hairSales.getDetailDiscountTotal());

                //-----------------------------------------
		//�S�̊���
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
               //�S�̊����Ɋ܂܂�Ă������ŕ�������
		taxTotal -= (this.getAccountSetting().getTax(this.getTotal(2).getValue(), 0l, this.getTaxRate()));
//                if (taxTotal < 0) {
//                    this.getTotal(4).setValue(0l);
//                } else {
//                    this.getTotal(4).setValue(taxTotal);
//                }
                  this.getTotal(4).setValue(taxTotal);
		//�������z
		if(this.getAccountSetting().getDisplayPriceType() == 1) {
		    this.getTotal(3).setValue(valueTotal - (this.getTotal(2).getValue() - this.getAccountSetting().getTax( this.getTotal(2).getValue(), 0l, this.getTaxRate())) );
		    this.getTotal(3).setValue(this.getTotal(3).getValue() + taxTotal);
		} else {
		    this.getTotal(3).setValue(valueTotal - this.getTotal(2).getValue());
		}
		
		Long temp = this.getSales().getPayment(0).getPaymentTotal();

		temp -= this.getTotal(3).getValue();

                //���ނ�
		this.getTotal(5).setValue(temp);

		//���|�����Z�b�g
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
     * ���|����ǂݍ��ށB
     *
     * @param customerID �ڋq�h�c
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
     * ���X�|���X�f�[�^��ǂݍ���
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
     * �\��f�[�^�̎{�p���ǂݍ���
     *
     * @param con ConnectionWrapper
     * @throws java.sql.SQLException SQLException
     */
    public void loadReservationBedData(ConnectionWrapper con) throws SQLException {
        if (hairSales.getCustomer().getCustomerID() == null) {
            // ���݂��Ȃ��`�[No.���\��f�[�^�ɓo�^����Ă�����u0�v�ōX�V����
            con.executeUpdate(getResetReservationSlipNo());
            throw new SQLException();
        }

        ResultSetWrapper rs = con.executeQuery(this.getSelectReservationSQL());

        int first = 0;
        while (rs.next()) {
            if (first == 0) {
                //1�s�ڂ̏ꍇ
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
                //�{�p�ǉ��Ή� start
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
                //�{�p�ǉ��Ή� end
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
     * �`�[�f�[�^��ǂݍ��ށB
     *
     * @param slipNo �`�[No.
     */
    public void load(MstShop shop, Integer slipNo) {
        hairSales.clear();
        hairSales.setShop(shop);
        hairSales.setSlipNo(slipNo);
        //nhanvt start add 20150326 Bug #35729
        hairSales.setAccountSetting(this.getMsSetting());
        //nhanvt end add 20150326 Bug #35729

        try {
            //�R�l�N�V�������擾
            ConnectionWrapper con = SystemInfo.getConnection();

            hairSales.loadAll(con);
            
            // IVS_Thanh start add 2014/07/11 Mashu_����v�\��
            hairSales.loadDataSaleMainStaff(con);
            // IVS_Thanh start add 2014/07/11 Mashu_����v�\��        

            //�\��f�[�^�̎{�p����擾
            loadReservationBedData(con);

            // ���X�|���X���擾
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
            //�R�l�N�V�������擾
            ConnectionWrapper con = SystemInfo.getConnection();

            hairSales.loadReservation(con, reservationNo);
            // IVS_Thanh start add 2014/07/11 Mashu_����v�\��'
            hairSales.loadDataReservaionMainStaff(con, reservationNo);
            // IVS_Thanh start add 2014/07/11 Mashu_����v�\��
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * �ǂݍ��񂾎x���f�[�^���œK������B
     *
     * @param index �C���f�b�N�X
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

        //�x���f�[�^��ǉ�
        for (DataPaymentDetail dpd1 : hairSales.getPayment(index)) {
            temp.add(dpd1);
        }

        temp.add(new DataPaymentDetail());

        hairSales.getPayment(index).clear();

        //�x�����׃f�[�^��ǉ�
        for (DataPaymentDetail dpd1 : temp) {
            hairSales.getPayment(index).add(dpd1);
        }
    }

    public boolean regist() {
        return regist(SystemInfo.getConnection());
    }

    /**
     * �o�^�������s���B
     *
     * @return true - ����
     */
    public boolean regist(ConnectionWrapper con) {
        boolean result = false;

        //hairSales.setShop(SystemInfo.getCurrentShop());
        hairSales.getPayment(0).setPaymentDate(hairSales.getSalesDate());
        //nhanvt start add 20150326 Bug #35729
        hairSales.setAccountSetting(this.getMsSetting());
        //nhanvt end add 20150326 Bug #35729
        try {
            //�g�����U�N�V�����J�n
            con.begin();

            //Start Add 20131004 lvut 
            DataSales hairSalesClone = (DataSales) this.getSales().clone();
            if (hairSalesClone.getSlipNo() == null) {
                hairSalesClone.setSlipNo(0);
            }
            //End Add 20131004 lvut

            //�ڋq����o�^
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

                // ���݂��Ȃ��`�[No.���\��f�[�^�ɓo�^����Ă�����u0�v�ōX�V����
                con.executeUpdate(getResetReservationSlipNo());

                // �\��f�[�^�̓`�[No.���݃`�F�b�N
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

            // ���X�|���X���ڂ�o�^����
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
            //�f�[�^��o�^
            if (hairSales.registAll(con, 0) && this.updateReservationStatus(con) && !isReservationSlipNo) {
                if (!hairSales.isNewAccount()) {
                    //�R�[�X�_����폜
                    if (!hairSales.deleteDataContract(con)) {
                        con.rollback();
                        return false;
                    }

                    //�R�[�X�������폜
                    if (!hairSales.deleteDataContractDigestion(con)) {
                        con.rollback();
                        return false;
                    }
                } 
                
                //�R�[�X�_�񗚗�o�^
                if(!hairSales.registCourseContract(con)){
                    con.rollback();
                    return false;
                }
                //�R�[�X��������o�^
                if(!hairSales.registConsumptionCourse(con)){
                    con.rollback();
                    return false;
                }

                //�\��f�[�^�̓o�^
                //�R�[�X�_��E�����R�[�X�̗\��͏�L���\�b�h�uupdateReservationStatus�v�ł͔j�]���Ă��邽�߂����ōēx�o�^���Ȃ���
                this.updateReservationStatusWithCourseContractAndContractDigestion(con);

                //�g�����U�N�V�����R�~�b�g
                con.commit();
                result = true;

                hairSales.setNewAccount(false);
            } else {
                //�g�����U�N�V�������[���o�b�N
                
                con.rollback();
                return false;
            }

        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        if (hairSales.isNewAccount() && !result) {

            MessageDialog.showMessageDialog(
                    null,
                    "�f�[�^�x�[�X�̐ڑ��Ɏ��s���܂����B\n�ēx�A�u�ꎞ�ۑ��v�܂��́u���Z�{�^���v����o�^���Ă��������B",
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
            //�R�[�X�_�񗚗�o�^
            hairSales.registCourseContract(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean registConsumptionCourse(ConnectionWrapper con) {
        try {
            //�R�[�X�_�񗚗�o�^
            hairSales.registConsumptionCourse(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean deleteCourseContract(ConnectionWrapper con) {
        try {
            //�R�[�X�_�񗚗��폜
            hairSales.deleteDataContract(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    public boolean deleteConsumptionCourse(ConnectionWrapper con) {
        try {
            //�R�[�X���������폜
            hairSales.deleteDataContractDigestion(con);
        } catch (SQLException e) {
            return false;
        }

        return true;
    }

    //�{�p���Ԃ̕ύX start add
    /**
     * �\��f�[�^�̎{�p�J�n���ԁE�{�p�I�����Ԃ��{�p���Ԃ���v�Z���X�V����
     * 
     * @return true:����I�� / false:�ُ�I��
     */
    public boolean updateReservationTime() {
        if (reservationNo == null) {
            return true;
        }
        ConnectionWrapper con = SystemInfo.getConnection();
        try {
            con.begin();
            
            //�\��w�b�_�̓Ǎ�
            DataReservation dr = new DataReservation();
            dr.setShop(this.getSales().getShop());
            dr.setReservationNo(reservationNo);
            dr.setOpeMinute(this.getOpeMinute());
            dr.setOpeSecond(this.getOpeSecond());
            dr.setStatus(this.getStatus());
            
            //�\��f�[�^�̎��ԍX�V
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
    //�{�p���Ԃ̕ύX start end
    
    private boolean updateReservationStatus(ConnectionWrapper con) {
        if (reservationNo == null) {
            return true;
        }

        try {
            //�\��w�b�_�̓Ǎ�
            DataReservation dr = new DataReservation();
            dr.setShop(this.getSales().getShop());
            dr.setReservationNo(reservationNo);

            dr.setStatus(this.getStatus());

            //Status 2:�ꎞ�ۑ��@3:���Z�{�^��  SubStaus 1:�{�p�҂� 2:�{�p��
            if (getStatus() == 2 && this.getSubStatus() != 2) {
                dr.setSubStatus(1);
            } else {
                dr.setSubStatus(2);
            }

            dr.setSlipNo(hairSales.getSlipNo());
            dr.setDesignated(hairSales.getDesignated());
            dr.setStaff(hairSales.getStaff());
            dr.setCustomer(hairSales.getCustomer());

            //�\�񖾍ׁE���̓Ǎ���
            loadSalesToReservation(con, dr);

            //�\��w�b�_�̍X�V
            if (!dr.updateStatus(con)) {
                return false;
            }

            // �X�L�b�v����v�̏ꍇ�͗\�񖾍ׂ��X�V����
            if (isSkipSales()) {

                //�{�p���Ԃ̕ύX start add
                if (SystemInfo.getDatabase().startsWith("pos_hair_nonail")) {
                    //3:���Z�{�^���������̏���
                    if (this.getStatus() == 3) {
                        dr.setOpeMinute(this.getOpeMinute());
                        dr.setOpeSecond(this.getOpeSecond());
                        if (!dr.updateTimeForSkipSales(con)) {
                            con.rollback();
                            return false;
                        }
                    }
                }
                //�{�p���Ԃ̕ύX end add
                
                //�\�񖾍ׂ̍폜
                if (!dr.deleteDetail(con)) {
                    con.rollback();
                    return false;
                }
                // ���̍폜
                if (!dr.deleteProportionally(con)) {
                    con.rollback();
                    return false;
                }

                //�\�񖾍ׁE���̓o�^
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
            //�\��w�b�_�̓Ǎ�
            DataReservation dr = new DataReservation();
            dr.setShop(this.getSales().getShop());
            dr.setReservationNo(reservationNo);

            dr.setStatus(this.getStatus());

            //Status 2:�ꎞ�ۑ��@3:���Z�{�^��  SubStaus 1:�{�p�҂� 2:�{�p��
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
            //����v�̖��ׂł̕\�����Ɨ\��f�[�^�̏��Ԃ�����Ȃ���\��o�^����
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

                // ���f�[�^���擾����
                loadSalesProportionallyToReservation(con, drd, dsd.getSlipDetailNo());

                dr.add(drd);
            }


//                        //�\�񖾍ׁE���̓Ǎ���
//                        loadSalesToReservation(con,dr);

//                        //�\��w�b�_�̍X�V
//                        if (!dr.updateStatus(con)) {
//                            return false;
//			}

            // �X�L�b�v����v�̏ꍇ�͗\�񖾍ׂ��X�V����
//                        if (isSkipSales()) {

            //�\�񖾍ׂ̍폜
//                            if (!dr.deleteDetail(con)) {
//                                con.rollback();
//                                return false;
//                            }
            // ���̍폜
//                            if ( !dr.deleteProportionally( con ) ) {
//                                con.rollback();
//                                return false;
//                            }

            //�\�񖾍ׁE���̓o�^
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
     * �`�[�f�[�^���Z�p���j���[�̂ݓǂݍ��݁A�\��f�[�^���쐬����B
     *
     * @param con �R�l�N�V����
     * @param dr �\��f�[�^
     * @return true - ����
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

                //�P���ڂ̃f�[�^�̏ꍇ
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

                // ���f�[�^���擾����
                loadSalesProportionallyToReservation(con, drd, rs.getInt("slip_detail_no"));

                dr.add(drd);
            }
        } catch (SQLException e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return true;
    }

    /**
     * �`�[���f�[�^��ǂݍ��݁A�\����f�[�^���쐬����B
     *
     * @param con �R�l�N�V����
     * @param drd �\��ڍ׃f�[�^
     * @param slipDetailNo �`�[�ڍ�NO
     */
    private boolean loadSalesProportionallyToReservation(ConnectionWrapper con, DataReservationDetail drd, Integer slipDetailNo) {
        // �\��ڍ׃f�[�^���̈��f�[�^���N���A����
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
     * �`�[�f�[�^���Z�p���j���[�̂ݓǂݍ��ނr�p�k�����擾����B
     *
     * @param dr �\��f�[�^
     * @return �`�[�f�[�^���Z�p���j���[�̂ݓǂݍ��ނr�p�k��
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
     * �`�[�����擾����r�p�k�����擾����B
     *
     * @param drd �\�񖾍׃f�[�^
     * @param slipDetailNo �`�[�ڍ�NO
     * @return �`�[�����擾����r�p�k��
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
     * �\��f�[�^��ǂݍ��ނr�p�k�����擾����B
     *
     * @return �\��f�[�^��ǂݍ��ނr�p�k��
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
        //�{�p���Ԓǉ��Ή� start
        sql.append("     ,dr.leave_time - dr.start_time as ope_time");
        //�{�p���Ԓǉ��Ή� end
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

        SystemInfo.getLogger().log(Level.WARNING, "getResetReservationSlipNo() �F ���݂��Ȃ��`�[No.���\��f�[�^�ɓo�^����Ă�����u0�v�ōX�V����N�G�����s");

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
