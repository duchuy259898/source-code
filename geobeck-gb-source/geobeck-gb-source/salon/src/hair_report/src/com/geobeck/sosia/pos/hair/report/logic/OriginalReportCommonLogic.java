/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.OriginalReportBean;
import com.geobeck.sosia.pos.hair.report.util.JExcelApi;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;

/**
 * オリジナル帳票出力-抽象クラス
 * 
 * @author user
 */
public abstract class OriginalReportCommonLogic {

    public enum Result {
         PRINT
         , SUCCESS
         , WARNING
         , ERROR
    };
    
    protected enum WriteItem {
        SHOP_NAME
        , STAFF_NAME
        , TARGET_RANGE
        , PERIOD_RANGE
        , ORDER
        , TAX
    };
    
    public static final String TEMPLATE_DIR = "/reports/";
    
    private OriginalReportBean bean = null;
    
    private JExcelApi report = null;
    
    class OriginalListBean implements Serializable {
        
        private String name = null;
        
        private Long num = null;
        
        private Double rate = null;

        /**
         * デフォルトコンストラクタ
         */
        public OriginalListBean() {
        }
        
        /**
         * コンストラクタ
         * 
         * @param name
         * @param num 
         */
        public OriginalListBean(String name, Long num) {
            this.name = name;
            this.num = num;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the num
         */
        public Long getNum() {
            return num;
        }

        /**
         * @param num the num to set
         */
        public void setNum(Long num) {
            this.num = num;
        }

        /**
         * @return the rate
         */
        public Double getRate() {
            return rate;
        }

        /**
         * @param rate the rate to set
         */
        public void setRate(Double rate) {
            this.rate = rate;
        }
        
        public void addBean(OriginalListBean bean) {
            if (bean != null) {
                if (bean.getNum() != null) {
                    if (this.num == null) {
                        this.num = 0L;
                    }
                    this.num += bean.getNum();
                }
                if (bean.getRate() != null) {
                    if (this.rate == null) {
                        this.rate = 0.00;
                    }
                    this.rate += bean.getRate();
                }
            }
        }
        
    }
    
    public OriginalReportCommonLogic(OriginalReportBean bean) {
        this.bean = bean;
    }

    /**
     * @return the bean
     */
    protected OriginalReportBean getBean() {
        return bean;
    }
    
    /**
     * @return the report
     */
    protected JExcelApi getReport() {
        return report;
    }
    
    public Result print() {
        this.report = new JExcelApi(this.getReportName());
        this.getReport().setTemplateFile(TEMPLATE_DIR + this.getTemplateName());
        
        ConnectionWrapper cw = SystemInfo.getConnection();
        try {
            Result result = this.process(cw);
            if (result == Result.PRINT) {
                this.getReport().openWorkbook();
                return Result.SUCCESS;
            }
            return result;
            
        } catch (Exception e) {
            SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            return Result.ERROR;
        } finally {
            try {
                if (cw != null) {
                    cw.close();
                }
            } catch (SQLException e) {
                SystemInfo.getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        }
    }
    
    protected void setRate(List<OriginalListBean> list) {
        long sumNum = 0L;
        for (OriginalListBean row : list) {
            if (row.getNum() != null) {
                sumNum += row.getNum();
            }
        }
        BigDecimal bgSumNum = new BigDecimal(sumNum);
        for (OriginalListBean row : list) {
            if (row.getNum() != null) {
                BigDecimal bgNum = new BigDecimal(row.getNum());
                if(bgSumNum.equals(BigDecimal.ZERO)) {
                    row.setRate(0.0);
                } else {
                    row.setRate(bgNum.divide(bgSumNum, 3, BigDecimal.ROUND_HALF_UP).doubleValue());
                }
            }
        }
    }
    
    protected void setRate(OriginalListBean bean, Long sumNum) {
        if (sumNum != null) {
            BigDecimal bgNum = new BigDecimal(sumNum);
            this.setRate(bean, bgNum);
        }
    }
    
    protected void setRate(OriginalListBean bean, BigDecimal sumNum) {
        if (bean.getNum() != null) {
            if (bean.getNum() <= 0
                    || sumNum.compareTo(BigDecimal.ZERO) == 0) {
                bean.setRate(0.0);
            } else {
                BigDecimal bgNum = new BigDecimal(bean.getNum());
                bean.setRate(bgNum.divide(sumNum, 3, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
        }
    }
    
    protected void writeShopName(int col, int row) {
        String name = this.getBean().getShopName();
        if (name == null) {
            name = this.getBean().getGroupName();
        }
        this.writeValue(col, row, name);
    }
    
    protected void writeStaffName(int col, int row) {
        this.writeValue(col, row, this.getBean().getStaffName());
    }
    
    protected void writeTargetRange(int col, int row) {
        String from = this.getBean().getTargetYmdFrom();
        String to = this.getBean().getTargetYmdTo();
        
        if (from != null && to != null) {
            this.writeValue(col, row, from + "～" + to);
        }
    }
    
    protected void writePeriodRange(int col, int row) {
        String from = this.getBean().getPeriodYmdFrom();
        String to = this.getBean().getPeriodYmdTo();
        
        if (OriginalReportBean.ExpReportType.RANKING.equals(this.getBean().getExpType()) || 
            OriginalReportBean.ExpReportType.MONTH_TRANSITION.equals(this.getBean().getExpType()) || 
            OriginalReportBean.ExpReportType.LANDING_FOCUS.equals(this.getBean().getExpType())) {
            if (this.getBean().isTargetPeriod01Flag()) {
                //期間選択時
                this.writeValue(col, row, this.getBean().getCalculationPeriod());
            } else {
                //年月日選択時
                if (from != null && to != null) {
                    this.writeValue(col, row, from + "～" + to);
                }
            }
        } else {
            if (from != null && to != null) {
                this.writeValue(col, row, from + "～" + to);
            }
        }
    }
    
    protected void writeOrder(int col, int row) {
        this.writeValue(col, row, this.getBean().getOrder());
    }
    
    protected void writeTax(int col, int row) {
        this.writeValue(col, row, this.getBean().getTax());
    }
    
    protected final void writeValue(int col, int row, Object value) {
        if (value != null) {
            this.getReport().setValue(col, row, value);
        }
    }
    
    protected final void writeVerticalItems(int col, int row, WriteItem... items) {
        if (items == null || items.length <= 0) {
            return;
        }
        for (WriteItem item : items) {
            switch (item) {
                case SHOP_NAME:
                    this.writeShopName(col, row);
                    break;
                case STAFF_NAME:
                    this.writeStaffName(col, row);
                    break;
                case TARGET_RANGE:
                    this.writeTargetRange(col, row);
                    break;
                case PERIOD_RANGE:
                    this.writePeriodRange(col, row);
                    break;
                case ORDER:
                    this.writeOrder(col, row);
                    break;
                case TAX:
                    this.writeTax(col, row);
                    break;
                default:
                    break;
            }
            row++;
        }
    }
    
//    protected <E extends QueryEditor> List<SalesDetailBean> readSalesDetails(
//            ConnectionWrapper cw, E editor) {
//        List<SalesDetailBean> results = new ArrayList<>();
//        StringBuilder query = new StringBuilder();
//        
//        return results;
//    }
//    
//    protected interface QueryEditor {
//        String edit(StringBuilder query);
//    }
    
    protected String getTemplateName() {
        return this.getReportName() + ".xls";
    }
    
    abstract protected String getReportName();
    
    abstract protected Result process(ConnectionWrapper connection) throws Exception;
    
}
