/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.OriginalReportBean;
import com.geobeck.sosia.pos.hair.report.beans.OriginalReportResultBean;
import com.geobeck.sosia.pos.hair.report.beans.SalesDetailBean;
import com.geobeck.sosia.pos.hair.report.beans.TargetResultBean;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author user
 */
public class OriginalReport02Logic extends OriginalReportCommonLogic {

    // ����f�[�^
    private Map<Integer, Map<Integer, OriginalReportResultBean>> saleses = null;
    
    // �ڕW�l
    private List<TargetResultBean> targetResults = null;
    
    // ���i�ѕ��z���X�g
    private List<OriginalListBean> priceTechnicList = null;
    
    // �I�t���X�g
    private List<OriginalListBean> offTechnicList = null;
    
    // ���񗈓X���@���X�g
    private LinkedHashMap<Integer, String> firstComingMotiveList = null;
    
    // ���񗈓X���@���ځu���̑��vID
    private static final Integer SONOTA_ID = -1;
    
    // ���񗈓X���@���ځu���̑��v
    private static final String SONOTA_NAME = "���̑�";
   
    // �Z�p����
    private BigDecimal technicTotal = null;
    
    // �{�p�ҏo�ΐ�
    private int staffWorkTimeCount = 0;
    
    // �I�t���X�g���ځu�I���̂݁v
    private static final String ONLY_ON = "�I���̂�";
    
    /**
     * �R���X�g���N�^
     * 
     * @param bean ��ʓ��͍��� 
     */
    public OriginalReport02Logic(OriginalReportBean bean) {
        super(bean);
    }

    @Override
    protected String getReportName() {
        return "���n�\�z";
    }

    @Override
    protected Result process(ConnectionWrapper connection) throws Exception {
        
        // �w�b�_�[�̏�������
        this.writeHeader();
        
        // ���т̎擾
        this.technicTotal = this.getTechnicTotal(connection);
        // �̔��f�[�^�̎擾
        this.saleses = this.readSalesDetails(connection);
        // �ڕW���уf�[�^�̎擾
        this.targetResults = this.readTargetResults(connection);
        // �X�^�b�t�o�ދ΃f�[�^�̎擾
        this.staffWorkTimeCount = this.getStaffWorkTimes(connection);
        // ���i�ѕ��̓��X�g
        this.priceTechnicList = this.getPriceTechnicList(connection);
        // �I�t���ރ��X�g
        this.offTechnicList = this.getOffTechnicList(connection);
        // �S���񗈓X���@���擾
        this.firstComingMotiveList = this.getFirstComingMotiveList(connection);
        
        // �f�[�^�̏�������
        this.writeMain();
        
        return Result.PRINT;
    }

    protected void writeHeader() {
        super.writeVerticalItems(4, 3
                , WriteItem.SHOP_NAME
                , WriteItem.TARGET_RANGE
//                , WriteItem.PERIOD_RANGE
                , WriteItem.TAX);
        
        // �c�Ɠ���
        super.writeValue(10, 4, this.getBean().getTargetDayRange());
        // �ғ�����
        super.writeValue(10, 5, this.getBean().getOptDayRange());
    }
    
    protected void writeMain() throws Exception {
        
        // ���ڕW�Δ�-���z-�ڕW
        BigDecimal sumTgtTechnic = BigDecimal.ZERO;
        // ���ڕW�Δ�-�ڋq��-�ڕW
        int sumTgtTechnicNum = 0;
        // �ڋq�\��-�V�K���v-�K�v��
        int sumTgtNewNum = 0;
        
        // �ڋq�\��-�V�K-�q���`�ڋq�\��-R-R10�`-�q��
        int[] sumRRNums = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        // �ڋq�\��-����������-�q��
        int sumFixAgainNum = 0;
        
        // ���񗈓X���@�J�E���g
        Map<String, Integer> firstComingMotiveCount = new HashMap<>();
        
        // �̔��f�[�^����ڋq�\���A���񗈓X���@���擾����B
        Iterator<Integer> shopIdIterator = this.saleses.keySet().iterator();
        while (shopIdIterator.hasNext()) {
            Integer shopId = shopIdIterator.next();
            Iterator<Integer> slipNoIterator = this.saleses.get(shopId).keySet().iterator();
            while (slipNoIterator.hasNext()) {
                Integer slipNo = slipNoIterator.next();
                
                // ���X�񐔂��Ƃ̃J�E���g�����Z����B
                OriginalReportResultBean bean = this.saleses.get(shopId).get(slipNo);
                int index = bean.getVisitNum() - 1;
                if (index >= 0) {
                    if (0 <= bean.getVisitNum().compareTo(sumRRNums.length)) {
                        index = sumRRNums.length - 1;
                    }
                    sumRRNums[index] += 1;
                }

                // �������̗L���𒲂ׂ�B
                boolean isAgain = false;
                for (SalesDetailBean detailBean : bean.getSalesDetails()) {
                    if (detailBean.getProductDivision().equals(3)) {
                        // �������̂���
                        isAgain = true;
                    }
                }
                if (isAgain) {
                    sumFixAgainNum++;
                }
                
                // ���񗈓X���@���J�E���g����B
                if (bean.getVisitNum().equals(Integer.valueOf(1))) {
                    Integer id = bean.getFirstComingMotiveClassId();
                    String motiveName = null;
                    if (id != null) {
                        motiveName = this.firstComingMotiveList.get(id);
                    }
                    
                    if (motiveName == null) {
                        // ���@�o�^������Ă��Ȃ����́A�}�X�^���̂ɗL���ȓ��@�f�[�^���Ȃ����̂́u���̑��v�Ƃ��Ĉ����B
                        motiveName = SONOTA_NAME;
                    }
                    
                    if (!firstComingMotiveCount.containsKey(motiveName)) {
                        firstComingMotiveCount.put(motiveName, Integer.valueOf(0));
                    }
                    Integer motiveCount = firstComingMotiveCount.get(motiveName);
                    firstComingMotiveCount.put(motiveName, motiveCount + 1);
                }
            }
        }
        
        // �ڕW�l���擾����B
        for (TargetResultBean bean : this.targetResults) {
            // �ڕW�Z�p����
            sumTgtTechnic = sumTgtTechnic.add(BigDecimal.valueOf(bean.getTargetTechnic()));
            // �ڕW�ڋq��
            sumTgtTechnicNum += bean.getTargetTechnicNum();
            // �ڕW�V�K�ڋq��
            sumTgtNewNum += bean.getTargetNewNum();
        }
        
        // �Ŕ����敪�ł���ΖڕW�Z�p���ォ��Ŕ����������s���B
        if (!super.getBean().isTaxFlag()) {
            Double taxRate = SystemInfo.getTaxRate(super.getBean().getTargetDateFrom());
            sumTgtTechnic = sumTgtTechnic.divide(new BigDecimal(taxRate).add(BigDecimal.ONE), 0, BigDecimal.ROUND_DOWN);
        }
               
        super.writeValue(5, 10, this.technicTotal.longValue());       // ���ڕW�Δ�-���z-����
        super.writeValue(7, 10, sumTgtTechnic);      // ���ڕW�Δ�-���z-�ڕW
        super.writeValue(7, 12, sumTgtTechnicNum);       // ���ڕW�Δ�-�ڋq��-�ڕW
        super.writeValue(14, 9, this.staffWorkTimeCount);       // �d�v�w��-�{�p�ҍ��v
        super.writeValue(4, 18, sumTgtNewNum);       // �ڋq�\��-�V�K���v-�K�v��
                
        // ���񗈓X���@�s�쐬
        
        // ���ڂ̏����s��
        final int INIT_ROW_COUNT = 2;
        
        // ���񗈓X���@���ڂ̊J�n�s
        final int FIRST_COMING_MOTIVE_LIST_START_ROW = 19;
        int row = FIRST_COMING_MOTIVE_LIST_START_ROW;
        
        // �����s���������X���@�f�[�^�������ꍇ�A�s��K�v�ȕ��ǉ�����B
        if(this.firstComingMotiveList.size() > INIT_ROW_COUNT) {
            this.getReport().insertRow(row, this.firstComingMotiveList.size() - INIT_ROW_COUNT);
        }
        
        // ���񗈓X���@�J�E���g��Ή�����Z���ɐݒ肷��B
        Iterator<Integer> motiveIte = this.firstComingMotiveList.keySet().iterator();
        while(motiveIte.hasNext()) {
            Integer motiveId = motiveIte.next();
            if(row != FIRST_COMING_MOTIVE_LIST_START_ROW) {
                // �P�s�ڈȊO�̃Z���͋󔒂ɂ���BmergeCells�ɂ��Z������������ہA�f�[�^������ƌx�����o�邽�߁B
                this.getReport().setValue(1, row, null);
                this.getReport().setValue(2, row, null);
            }
            this.getReport().mergeCells(3, row, 4, row);
            this.getReport().mergeCells(5, row, 6, row);
            
            String motive = this.firstComingMotiveList.get(motiveId);
            // ���񗈓X���@���̂��Z���ɏ������ށB
            super.writeValue(3, row, motive);
            // �Ή�����J�E���g���Z���ɏ������ށB
            super.writeValue(5, row, firstComingMotiveCount.get(motive));
            ++row;
        }
        
        this.getReport().mergeCells(1, FIRST_COMING_MOTIVE_LIST_START_ROW, 1, row - 1);
        this.getReport().mergeCells(2, FIRST_COMING_MOTIVE_LIST_START_ROW, 2, row - 1);
        
        // R�ݒ�
        final int ROWS_TO_R = 1;
        row += ROWS_TO_R;
        for(int i = 1; i < sumRRNums.length; ++i) {
            super.writeValue(5, row, sumRRNums[i]);
            ++row;
        }
        
        // �������ݒ�
        super.writeValue(5, row, sumFixAgainNum);
        
        // ���i�ѕ��z�ݒ�
        final int ROWS_TO_PRICE_TECHNIC = 4;
        row += ROWS_TO_PRICE_TECHNIC;
        // ���ޖ��f�[�^���������s����葽���ꍇ�́A�K�v�s�����C���T�[�g����B
        if (this.priceTechnicList.size() > INIT_ROW_COUNT) {
            int addRow = this.priceTechnicList.size() - INIT_ROW_COUNT;
            this.getReport().insertRow(row, addRow);
        }
        for (OriginalListBean result : this.priceTechnicList) {
            this.getReport().mergeCells(1, row, 6, row);
            super.writeValue(1, row, result.getName());
            super.writeValue(7, row, result.getNum());
            row++;
        }
        
        // �I�t���ސݒ�
        final int ROWS_TO_PRICE_OFF_TECHNIC = 4;
        row += ROWS_TO_PRICE_OFF_TECHNIC;
        // ���ޖ��f�[�^���������s����葽���ꍇ�́A�K�v�s�����C���T�[�g����B
        if (this.offTechnicList.size() > INIT_ROW_COUNT) {
            int addRow = this.offTechnicList.size() - INIT_ROW_COUNT;
            this.getReport().insertRow(row, addRow);
        }
        for (OriginalListBean result : this.offTechnicList) {
            this.getReport().mergeCells(1, row, 6, row);
            super.writeValue(1, row, result.getName());
            super.writeValue(7, row, result.getNum());
            row++;
        }
    }
    
    protected Map<Integer, Map<Integer, OriginalReportResultBean>> readSalesDetails(ConnectionWrapper conn) throws Exception {
        // �̔��f�[�^�Ɨ��X���@���擾����
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
	sb.append("  a.shop_id ");
	sb.append("  , a.slip_no ");
	sb.append("  , a.sales_date ");
	sb.append("  , a.customer_id ");
	sb.append("  , a.designated_flag ");
	sb.append("  , a.staff_id ");
	sb.append("  , a.visit_num ");
	sb.append("  , a.visited_memo ");
	sb.append("  , a.next_visit_date ");
	sb.append("  , a.reappearance_date ");
	sb.append("  , b.slip_detail_no ");
	sb.append("  , b.product_division ");
	sb.append("  , b.product_id ");
	sb.append("  , b.product_num ");
	sb.append("  , b.product_value ");
	sb.append("  , b.discount_rate ");
	sb.append("  , b.discount_value ");
	sb.append("  , b.designated_flag ");
	sb.append("  , b.staff_id ");
	sb.append("  , b.approached_flag ");
	sb.append("  , b.contract_shop_id ");
	sb.append("  , b.contract_no ");
	sb.append("  , b.contract_detail_no ");
	sb.append("  , c.first_coming_motive_class_id ");
	sb.append("from ");
	sb.append("  data_sales a JOIN data_sales_detail b ");
	sb.append("    ON a.shop_id = b.shop_id ");
	sb.append("    AND a.slip_no = b.slip_no ");
	sb.append("    AND b.delete_date IS NULL ");
	sb.append("  LEFT OUTER JOIN mst_customer c ");
	sb.append("    ON a.customer_id = c.customer_id ");
	sb.append("    AND c.delete_date IS NULL ");
	sb.append("WHERE ");
        sb.append("  a.delete_date IS NULL ");
	sb.append("  AND b.product_division IN (1, 3) ");
        
        if (super.getBean().getShopId() != null) {
            sb.append("   AND a.shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
        
        sb.append("   AND a.sales_date >= ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        sb.append("   AND a.sales_date <= ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));

        //SQL���s
        ResultSetWrapper rs = conn.executeQuery(sb.toString());

        // ���ʂ�X��ID�A�`�[�ԍ����ƂɃ}�b�v�ɒǉ����Ă����B
        Map<Integer, Map<Integer, OriginalReportResultBean>> results = new HashMap<>();
        while (rs.next()) {
            Integer shopId = rs.getInt("shop_id");
            Integer slipNo = rs.getInt("slip_no");
            
            if(!results.containsKey(shopId)) {
                results.put(shopId, new HashMap<Integer, OriginalReportResultBean>());
            } 
            if(!results.get(shopId).containsKey(slipNo)) {
                // �X��ID�A�`�[�ԍ��ɂЂ��Â�����f�[�^��bean�ɐݒ肷��B
                OriginalReportResultBean bean = new OriginalReportResultBean();
                bean.setShopId(shopId);
                bean.setSlipNo(slipNo);
                bean.setSalesDate(rs.getDate("sales_date"));
                bean.setVisitNum(rs.getInt("visit_num"));
                Integer motiveId = Integer.valueOf(rs.getInt("first_coming_motive_class_id"));
                // �擾���ʂ�null�������ꍇ�Anull��ݒ肵�Ȃ����BgetInt��null��0�ɕς��Ă��܂����߁B
                if (rs.wasNull()) {
                    motiveId = null;
                }
                bean.setFirstComingMotiveClassId(motiveId);
                bean.setCustomerId(rs.getInt("customer_id"));
                results.get(shopId).put(slipNo, bean);
            }
            OriginalReportResultBean bean = results.get(shopId).get(slipNo);
            
            SalesDetailBean detail = new SalesDetailBean();
            detail.setShopId(rs.getInt("shop_id"));
            detail.setSlipNo(rs.getInt("slip_no"));
            detail.setSlipDetailNo(rs.getInt("slip_detail_no"));
            detail.setProductDivision(rs.getInt("product_division"));
            detail.setProductId(rs.getInt("product_id"));
            detail.setProductValue(rs.getBigDecimal("product_value"));
            detail.setDiscountRate(rs.getBigDecimal("discount_rate"));
            detail.setDiscountValue(rs.getBigDecimal("discount_value"));
            detail.setDesignatedFlag(rs.getBoolean("designated_flag"));
            detail.setStaffId(rs.getInt("staff_id"));
            detail.setApproachedFlag(rs.getBoolean("approached_flag"));
            detail.setContractNo(rs.getInt("contract_no"));
            detail.setContractDetailNo(rs.getInt("contract_detail_no"));
            detail.setContractShopId(rs.getInt("contract_shop_id"));

            bean.getSalesDetails().add(detail);
        }
        
        return results;
    }
    
    protected BigDecimal getTechnicTotal(ConnectionWrapper conn) throws Exception {
        String taxColumnName;
        if (this.getBean().isTaxFlag()) {
            taxColumnName = "discount_detail_value_in_tax";
        } else {
            taxColumnName = "discount_detail_value_no_tax";
        }
        // ���т��擾����
        StringBuilder query = new StringBuilder();
        query.append("SELECT");
        query.append("  COALESCE( ");
        query.append("    SUM( ");
        query.append("      CASE ");
        query.append("        WHEN dsd.product_division IN (1, 3) ");
        query.append("          THEN dsd.").append(taxColumnName).append(" ");
        query.append("        ELSE 0 ");
        query.append("        END");
        query.append("    ), 0 ");
        query.append("  ) AS technic_total ");
	query.append("FROM ");
	query.append("  view_data_sales_detail_valid dsd ");
	query.append("  JOIN data_sales ds ON ds.shop_id = dsd.shop_id AND ds.slip_no = dsd.slip_no ");
	query.append("WHERE ");
	query.append("   ds.delete_date IS NULL ");
        query.append("   AND dsd.sales_date >= ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        query.append("   AND dsd.sales_date <= ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        if (super.getBean().getShopId() != null) {
            query.append(" AND dsd.shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }

        //SQL���s
        ResultSetWrapper rs = conn.executeQuery(query.toString());

        BigDecimal total = BigDecimal.ZERO;
        if (rs.next()) {
            total = rs.getBigDecimal(1);
        }
        return total;
    }
    
    protected List<TargetResultBean> readTargetResults(ConnectionWrapper conn) throws Exception {
        // �ڕW�f�[�^�擾
        StringBuilder query = new StringBuilder();
        query.append(" SELECT");
        query.append("   shop_id");
        query.append("   , year");
        query.append("   , month");
        query.append("   , result_technic");
        query.append("   , result_item");
        query.append("   , result_in");
        query.append("   , result_new");
        query.append("   , result_k");
        query.append("   , result_set");
        query.append("   , result_s");
        query.append("   , result_hd");
        query.append("   , result_p");
        query.append("   , result_stp");
        query.append("   , result_tr");
        query.append("   , result_etc");
        query.append("   , result_crm");
        query.append("   , result_mon");
        query.append("   , target_technic");
        query.append("   , target_item");
        query.append("   , target_in");
        query.append("   , target_new");
        query.append("   , target_k");
        query.append("   , target_set");
        query.append("   , target_s");
        query.append("   , target_hd");
        query.append("   , target_p");
        query.append("   , target_stp");
        query.append("   , target_tr");
        query.append("   , target_etc");
        query.append("   , target_crm");
        query.append("   , target_mon");
        query.append("   , open_days");
        query.append("   , insert_date");
        query.append("   , update_date");
        query.append("   , delete_date");
        query.append("   , target_course");
        query.append("   , target_digestion");
        query.append("   , target_nomination_value");
        query.append("   , target_technic_num");
        query.append("   , target_nomination_num");
        query.append("   , target_new_num");
        query.append("   , target_introduction_num");
        query.append("   , target_item_num");
        query.append("   , target_staff_per_sales");
        query.append("   , target_staff_num");
        query.append("   , target_item_rate");
        query.append("   , target_next_reserve_rate");
        query.append("   , target_reserve_close_rate");
        query.append("   , repert_30_new");
        query.append("   , repert_45_new");
        query.append("   , repert_60_new");
        query.append("   , repert_90_new");
        query.append("   , repert_120_new");
        query.append("   , repert_150_new");
        query.append("   , repert_180_new");
        query.append("   , repert_30_fix");
        query.append("   , repert_45_fix");
        query.append("   , repert_60_fix");
        query.append("   , repert_90_fix");
        query.append("   , repert_120_fix");
        query.append("   , repert_150_fix");
        query.append("   , repert_180_fix");
        query.append(" FROM");
        query.append("   data_target_result");
        query.append(" WHERE delete_date IS NULL");
        
        if (super.getBean().getShopId() != null) {
            query.append("   AND shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
        
        query.append("   AND year = ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetFromYear()));
        query.append("   AND month = ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetFromMonth()));
        
        //SQL���s
        ResultSetWrapper rs = conn.executeQuery(query.toString());

        List<TargetResultBean> results = new ArrayList<>();
        while (rs.next()) {
            TargetResultBean row = new TargetResultBean();
            row.setShopId(rs.getInt("shop_id"));
            row.setYear(rs.getInt("year"));
            row.setMonth(rs.getInt("month"));
            row.setResultTechnic(rs.getInt("result_technic"));
            row.setResultItem(rs.getInt("result_item"));
            row.setResultIn(rs.getInt("result_in"));
            row.setResultNew(rs.getInt("result_new"));
            row.setResultK(rs.getInt("result_k"));
            row.setResultSet(rs.getInt("result_set"));
            row.setResultS(rs.getInt("result_s"));
            row.setResultHd(rs.getInt("result_hd"));
            row.setResultP(rs.getInt("result_p"));
            row.setResultStp(rs.getInt("result_stp"));
            row.setResultTr(rs.getInt("result_tr"));
            row.setResultEtc(rs.getInt("result_etc"));
            row.setResultCrm(rs.getInt("result_crm"));
            row.setResultMon(rs.getInt("result_mon"));
            row.setTargetTechnic(rs.getInt("target_technic"));
            row.setTargetItem(rs.getInt("target_item"));
            row.setTargetIn(rs.getInt("target_in"));
            row.setTargetNew(rs.getInt("target_new"));
            row.setTargetK(rs.getInt("target_k"));
            row.setTargetSet(rs.getInt("target_set"));
            row.setTargetS(rs.getInt("target_s"));
            row.setTargetHd(rs.getInt("target_hd"));
            row.setTargetP(rs.getInt("target_p"));
            row.setTargetStp(rs.getInt("target_stp"));
            row.setTargetTr(rs.getInt("target_tr"));
            row.setTargetEtc(rs.getInt("target_etc"));
            row.setTargetCrm(rs.getInt("target_crm"));
            row.setTargetMon(rs.getInt("target_mon"));
            row.setOpenDays(rs.getInt("open_days"));
            row.setInsertDate(rs.getDate("insert_date"));
            row.setUpdateDate(rs.getDate("update_date"));
            row.setDeleteDate(rs.getDate("delete_date"));
            row.setTargetCourse(rs.getInt("target_course"));
            row.setTargetDigestion(rs.getInt("target_digestion"));
            row.setTargetNominationValue(rs.getInt("target_nomination_value"));
            row.setTargetTechnicNum(rs.getInt("target_technic_num"));
            row.setTargetNominationNum(rs.getInt("target_nomination_num"));
            row.setTargetNewNum(rs.getInt("target_new_num"));
            row.setTargetIntroductionNum(rs.getInt("target_introduction_num"));
            row.setTargetItemNum(rs.getInt("target_item_num"));
            row.setTargetStaffPerSales(rs.getInt("target_staff_per_sales"));
            row.setTargetStaffNum(rs.getInt("target_staff_num"));
            row.setTargetItemRate(rs.getInt("target_item_rate"));
            row.setTargetNextReserveRate(rs.getInt("target_next_reserve_rate"));
            row.setTargetReserveCloseRate(rs.getInt("target_reserve_close_rate"));
            row.setRepert30New(rs.getInt("repert_30_new"));
            row.setRepert45New(rs.getInt("repert_45_new"));
            row.setRepert60New(rs.getInt("repert_60_new"));
            row.setRepert90New(rs.getInt("repert_90_new"));
            row.setRepert120New(rs.getInt("repert_120_new"));
            row.setRepert150New(rs.getInt("repert_150_new"));
            row.setRepert180New(rs.getInt("repert_180_new"));
            row.setRepert30Fix(rs.getInt("repert_30_fix"));
            row.setRepert45Fix(rs.getInt("repert_45_fix"));
            row.setRepert60Fix(rs.getInt("repert_60_fix"));
            row.setRepert90Fix(rs.getInt("repert_90_fix"));
            row.setRepert120Fix(rs.getInt("repert_120_fix"));
            row.setRepert150Fix(rs.getInt("repert_150_fix"));
            row.setRepert180Fix(rs.getInt("repert_180_fix"));

            results.add(row);
        }
        
        return results;

    }
    
    private int getStaffWorkTimes(ConnectionWrapper conn) throws Exception {
        // �Αӏ��擾
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
	sb.append("  count(working_start_time) ");
	sb.append("FROM ");
	sb.append("  data_staff_work_time a ");
	sb.append("WHERE ");
	sb.append("  a.delete_date IS NULL ");

        sb.append("   AND a.working_date >= ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        sb.append("   AND a.working_date <= ").append(
                SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));

        if (super.getBean().getShopId() != null) {
            sb.append("   AND shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }

        //SQL���s
        ResultSetWrapper rs = conn.executeQuery(sb.toString());
        
        int count = 0;
        if (rs.next()) {
            count = rs.getInt(1);
        }
        
        return count;
    }
    
    private List<OriginalListBean> getPriceTechnicList(ConnectionWrapper conn) throws Exception {
        // ���i�ѕ��z���擾
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
	sb.append("  m1.technic_class_id ");
	sb.append("  , m1.technic_class_name ");
	sb.append("  , SUM( ");
	sb.append("    CASE ");
	sb.append("      WHEN ( ");
	sb.append("        t1.shop_id = t2.shop_id ");
	sb.append("        AND t1.slip_no = t2.slip_no ");
	sb.append("      ) ");
	sb.append("        THEN t1.product_num ");
	sb.append("      ELSE 0 ");
	sb.append("      END ");
	sb.append("  ) AS product_num ");
	sb.append("  , 0 AS order_no ");
	sb.append("  , m1.display_seq ");
	sb.append("FROM ");
	sb.append("  mst_technic_class AS m1 ");
	sb.append("  INNER JOIN mst_technic AS m2 ");
	sb.append("    ON (m1.technic_class_id = m2.technic_class_id) ");
	sb.append("  INNER JOIN mst_use_product AS m3 ");
	sb.append("    ON ( ");
	sb.append("      m2.technic_id = m3.product_id ");
	sb.append("      AND m3.product_division = 1 ");
	sb.append("    ) ");
	sb.append("  LEFT OUTER JOIN data_sales_detail AS t1 ");
	sb.append("    ON ( ");
	sb.append("      t1.shop_id = m3.shop_id ");
	sb.append("      AND t1.product_id = m3.product_id ");
	sb.append("      AND t1.product_division in (1, 3) ");
	sb.append("    ) ");
	sb.append("  LEFT OUTER JOIN data_sales AS t2 ");
	sb.append("    ON ( ");
	sb.append("      t2.shop_id = m3.shop_id ");
	sb.append("      AND t2.slip_no = t1.slip_no ");
        
        sb.append("      AND t2.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        sb.append("      AND t2.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
	
        sb.append("    ) ");
	sb.append("WHERE ");
	sb.append("  t1.delete_date IS NULL ");
	sb.append("  AND t2.delete_date IS NULL ");
	sb.append("  AND m1.technic_class_id <> 23 ");
	sb.append("  AND m2.technic_no <> 'off004' ");
        
	if (super.getBean().getShopId() != null) {
            sb.append("   AND m3.shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
        
	sb.append("GROUP BY ");
	sb.append("  m1.technic_class_id ");
	sb.append("  , m1.technic_class_name ");
	sb.append("  , order_no ");
	sb.append("  , m1.display_seq ");
	sb.append("UNION ALL ");
        
        // �I�t�̂ݎ擾�p
	sb.append("SELECT ");
	sb.append("  m1.technic_class_id ");
	sb.append("  , m2.technic_name ");
	sb.append("  , SUM( ");
	sb.append("    CASE ");
	sb.append("      WHEN ( ");
	sb.append("        t1.shop_id = t2.shop_id ");
	sb.append("        AND t1.slip_no = t2.slip_no ");
	sb.append("      ) ");
	sb.append("        THEN t1.product_num ");
	sb.append("      ELSE 0 ");
	sb.append("      END ");
	sb.append("  ) AS product_num ");
	sb.append("  , 1 AS order_no ");
	sb.append("  , m1.display_seq ");
	sb.append("FROM ");
	sb.append("  mst_technic_class AS m1 ");
	sb.append("  INNER JOIN mst_technic m2 ");
	sb.append("    ON (m1.technic_class_id = m2.technic_class_id) ");
	sb.append("  LEFT OUTER JOIN data_sales_detail AS t1 ");
	sb.append("    ON ( ");
	sb.append("      t1.product_id = m2.technic_id ");
	sb.append("      AND t1.product_division in (1, 3) ");
        
	if (super.getBean().getShopId() != null) {
            sb.append("   AND t1.shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
	
        sb.append("    ) ");
	sb.append("  LEFT OUTER JOIN data_sales AS t2 ");
	sb.append("    ON ( ");
        sb.append("      t2.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        sb.append("      AND t2.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        
        if (super.getBean().getShopId() != null) {
            sb.append("   AND t2.shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
	sb.append("      AND t2.slip_no = t1.slip_no ");
	sb.append("    ) ");
	sb.append("WHERE ");
	sb.append("  m2.technic_no = 'off004' ");
	sb.append("  AND m1.technic_class_id = 23 ");
	sb.append("GROUP BY ");
	sb.append("  m1.technic_class_id ");
	sb.append("  , m2.technic_name ");
	sb.append("  , order_no ");
	sb.append("  , m1.display_seq ");
	sb.append("ORDER BY ");
	sb.append("  order_no ");
	sb.append("  , display_seq ");
        
        //SQL���s
        ResultSetWrapper rs = conn.executeQuery(sb.toString());

        List<OriginalListBean> results = new ArrayList<>();
        while (rs.next()) {
            OriginalListBean row = new OriginalListBean();
            row.setName(rs.getString("technic_class_name"));
            row.setNum(rs.getLong("product_num"));

            results.add(row);
        }
        
        return results;
        
    }
    
    private List<OriginalListBean> getOffTechnicList(ConnectionWrapper conn) throws Exception {
        // �I�t���ޏ��擾
        StringBuilder query = new StringBuilder();
        query.append(" SELECT");
        query.append("   sub.row_index");
        query.append("   , sub.technic_name");
        query.append("   , SUM(sub.product_num) AS product_num");
        query.append(" FROM (");
        query.append("   SELECT");
        query.append("     1 AS row_index");
        query.append("     , am1.technic_name");
	query.append("     , CASE ");
	query.append("          WHEN ( ");
	query.append("           at1.shop_id = at2.shop_id ");
	query.append("           AND at1.slip_no = at2.slip_no ");
	query.append("         ) ");
	query.append("           THEN at1.product_num ");
	query.append("         ELSE 0 ");
	query.append("         END AS product_num ");
        query.append("     , am1.display_seq");
        query.append("   FROM");
        query.append("     mst_technic AS am1");
        query.append("   LEFT OUTER JOIN");
        query.append("     data_sales_detail AS at1");
        query.append("     ON (at1.product_id = am1.technic_id AND at1.product_division in (1, 3))");
        query.append("   LEFT OUTER JOIN");
        query.append("     data_sales AS at2");
        query.append("     ON (");
        query.append("       at2.shop_id = at1.shop_id");
        query.append("       AND at2.slip_no = at1.slip_no");
        
        if (super.getBean().getShopId() != null) {
            query.append("       AND at2.shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
        
        query.append("       AND at2.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        query.append("       AND at2.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        
        query.append("     )");
        query.append("   WHERE");
        query.append("     at1.delete_date IS NULL");
        query.append("     AND am1.technic_class_id = 23");
        query.append("     AND am1.technic_no <> 'off004'");
        query.append("   UNION ALL");
        
        // �I���̂ݎ擾�p
        query.append(" SELECT ");
	query.append("  2 AS row_index ");
	query.append("  , '" + ONLY_ON + "' AS technic_name ");
	query.append("  , 1 AS product_num ");
	query.append("  , 0 AS display_seq ");
	query.append("FROM ");
	query.append("  data_sales bt2 ");
	query.append("WHERE ");
	query.append("  bt2.delete_date IS NULL ");
        
        if (super.getBean().getShopId() != null) {
            query.append("  AND bt2.shop_id = ").append(
                    SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
        
        query.append("  AND bt2.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        query.append("  AND bt2.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        
	query.append("  AND NOT EXISTS ( ");
	query.append("    SELECT ");
	query.append("      1 ");
	query.append("    FROM ");
	query.append("      mst_technic AS bsm1 ");
	query.append("      INNER JOIN data_sales_detail bst1 ");
	query.append("        ON bst1.product_id = bsm1.technic_id ");
	query.append("        AND bst1.product_division IN (1, 3) ");
	query.append("        AND bst1.shop_id = bt2.shop_id ");
	query.append("        AND bst1.slip_no = bt2.slip_no ");
        query.append("        AND bst1.delete_date is null ");
	query.append("    WHERE ");
	query.append("      bsm1.technic_class_id = 23 ");
	query.append("  ) ");
        query.append(" ) AS sub");
        query.append(" GROUP BY");
        query.append("   sub.row_index, sub.technic_name, sub.display_seq");
        query.append(" ORDER BY");
        query.append("   sub.row_index, display_seq");
        
        //SQL���s
        ResultSetWrapper rs = conn.executeQuery(query.toString());
        rs.beforeFirst();

        List<OriginalListBean> results = new ArrayList<>();
        while (rs.next()) {
            OriginalListBean row = new OriginalListBean();
            row.setName(rs.getString("technic_name"));
            row.setNum(rs.getLong("product_num"));

            results.add(row);
        }
        
        // �I���݂̂����X�g�Ɋ܂܂�Ă��Ȃ��ꍇ�͒ǉ�����B
        if (results.isEmpty() || !StringUtils.equals(ONLY_ON, results.get(results.size() - 1).getName())) {
            OriginalListBean row = new OriginalListBean();
            row.setName(ONLY_ON);
            row.setNum(0L);
            results.add(row);
        }
        
        return results;
    }
    
    private LinkedHashMap<Integer, String> getFirstComingMotiveList(ConnectionWrapper conn) throws Exception {
        // ���񗈓X���@�擾
        final String QUERY = 
                "SELECT "
              + "  a.first_coming_motive_class_id, "
              + "  a.first_coming_motive_name "
              + "FROM mst_first_coming_motive a "
              + "WHERE a.delete_date IS NULL "
              + "ORDER BY a.display_seq";
        //SQL���s
        ResultSetWrapper rs = conn.executeQuery(QUERY);

        boolean isExistSonota = false;
        LinkedHashMap<Integer, String> results = new LinkedHashMap<>();
        while (rs.next()) {
            String name = rs.getString("first_coming_motive_name");
            results.put(rs.getInt("first_coming_motive_class_id"), name);
            
            if (SONOTA_NAME.equals(name)) {
                isExistSonota = true;
            }
        }
        
        // �������ځu���̑��v���}�X�^�ɂȂ������ꍇ�́A�����Œǉ�����B
        // ���@���o�^����Ă��Ȃ��ꍇ�Ȃǂ̏W���Ƃ��Ďg�p���邽�߁A�u���̑��v�͕K�v�ɂȂ�B
        if (!isExistSonota) {
            results.put(SONOTA_ID, SONOTA_NAME);
        }
        return results;
    }
}
