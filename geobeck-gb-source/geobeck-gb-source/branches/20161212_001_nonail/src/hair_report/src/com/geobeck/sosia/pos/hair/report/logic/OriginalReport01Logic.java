/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.beans.OriginalReportBean;
import com.geobeck.sosia.pos.master.company.MstShop;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.sosia.pos.master.company.MstStaffs;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.ConnectionWrapper;
import com.geobeck.sql.ResultSetWrapper;
import com.geobeck.util.SQLUtil;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author user
 */
public class OriginalReport01Logic extends OriginalReportCommonLogic {

    /**
     * 再来算出期間項目
     */
    static final String[] OUTPUT_PERIOD_COLUMNS = {
        "R2"
        , "R"
    };
    
    /**
     * 対象期間項目
     */
    static final String[] OUTPUT_TARGET_COLUMNS = {
        "TO率"
        , "客数"
        , "指名数"
        , "口コミ数（良）"
        , "口コミ数（悪）"
        , "お直し数"
        , "お直し率"
        , "単価"
        , "売上"
    };
    
    static final String[] OUTPUT_OTHER_COLUMNS = {
        "新規顧客数"
        , "既存顧客数"
    };
    
    static final Map<String, String> COLUMN_OUTPUT_MAP = new HashMap<String, String>() {
        {
            this.put(OUTPUT_PERIOD_COLUMNS[0], "R2（新規再来）");
            this.put(OUTPUT_PERIOD_COLUMNS[1], "R（既存再来）");
        }
    };
    
    /** 帳票に出力する明細リスト */
    private final List<OriginalReport01Bean> reportList = new ArrayList<>();
    
    class OriginalReport01Bean implements Serializable {
        
        /** ショップID */
        private Integer shopId = null;
        
        /** ショップ名 */
        private String shopName = null;
        
        /** 担当者ID */
        private Integer staffId = null;
        
        /** 担当者名 */
        private String staffName = null;
        
        
        
        /** 詳細 */
        private Map<String, OriginalReport01DetailBean> detail = new LinkedHashMap<>();
        
        /**
         * @return the shopId
         */
        public Integer getShopId() {
            return shopId;
        }

        /**
         * @param shopId the shopId to set
         */
        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        /**
         * @return the shopName
         */
        public String getShopName() {
            return shopName;
        }

        /**
         * @param shopName the shopName to set
         */
        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        /**
         * @return the staffId
         */
        public Integer getStaffId() {
            return staffId;
        }

        /**
         * @param staffId the staffId to set
         */
        public void setStaffId(Integer staffId) {
            this.staffId = staffId;
        }

        /**
         * @return the staffName
         */
        public String getStaffName() {
            return staffName;
        }

        /**
         * @param staffName the staffName to set
         */
        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        /**
         * @return the detail
         */
        public Map<String, OriginalReport01DetailBean> getDetail() {
            return detail;
        }

        /**
         * @param detail the detail to set
         */
        public void setDetail(Map<String, OriginalReport01DetailBean> detail) {
            this.detail = detail;
        }
        
        /**
         * 一覧の明細行を集計し、取得します。
         * 
         * @return 集計結果
         */
        public OriginalReport01DetailBean getSummaryDetail() {
            OriginalReport01DetailBean tmp = new OriginalReport01DetailBean();
            if (this.getDetail().isEmpty()) {
                OriginalReport01DetailBean empty = new OriginalReport01DetailBean();
                empty.setBadNum(new OriginalListBean(null, 0L));
                empty.setGoodNum(new OriginalListBean(null, 0L));
                empty.setRr(new OriginalListBean(null, 0L));
                empty.setR2(new OriginalListBean(null, 0L));
                empty.setDesignatedNum(new OriginalListBean(null, 0L));
                empty.setProceedsNum(new OriginalListBean(null, 0L));
                empty.setProductNum(new OriginalListBean(null, 0L));
                empty.setRetouchNum(new OriginalListBean(null, 0L));
                empty.setRetouchNumEachSlip(new OriginalListBean(null, 0L));
                empty.setRetouchRate(new OriginalListBean(null, 0L));
                empty.setSalesNum(new OriginalListBean(null, 0L));
                empty.setSalesDetailNum(new OriginalListBean(null, 0L));
                empty.setToRate(new OriginalListBean(null, 0L));
                empty.setNewSalesNum(new OriginalListBean(null, 0L));
                empty.setFixedSalesNum(new OriginalListBean(null, 0L));
                tmp.addBean(empty);
            } else {
                for (String key : this.getDetail().keySet()) {
                    tmp.addBean(this.getDetail().get(key));
                }
            }
            return tmp;
        }

    }
    
    class OriginalReport01DetailBean implements Serializable {
        
        /** 名称 */
        private String name;
        
        /** R2（新規再来） */
        private OriginalListBean r2 = null;
        
        /** R全（新規・再来含） */
        private OriginalListBean rr = null;
        
        /** 新規顧客数 */
        private OriginalListBean newSalesNum = null;
        
        /** 既存顧客数 */
        private OriginalListBean fixedSalesNum = null;
        
        /** TO率（時間超過率） */
        private OriginalListBean toRate = null;
        
        /** 客数 */
        private OriginalListBean salesNum = null;
        
        /** 施術数 */
        private OriginalListBean salesDetailNum = null;
        
        /** 指名数 */
        private OriginalListBean designatedNum = null;
        
        /** 口コミ数（良い） */
        private OriginalListBean goodNum = null;
        
        /** 口コミ数（悪い） */
        private OriginalListBean badNum = null;
        
        /** お直し数(明細ごと) */
        private OriginalListBean retouchNum = null;
        
        /** お直し数(売上ごと) */
        private OriginalListBean retouchNumEachSlip = null;
        
        /** お直し率 */
        private OriginalListBean retouchRate = null;
        
        /** 単価 */
        private OriginalListBean productNum = null;
        
        /** 売上 */
        private OriginalListBean proceedsNum = null;

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
         * @return the r2
         */
        public OriginalListBean getR2() {
            return r2;
        }

        /**
         * @param r2 the r2 to set
         */
        public void setR2(OriginalListBean r2) {
            this.r2 = r2;
        }

        /**
         * @return the rr
         */
        public OriginalListBean getRr() {
            return rr;
        }

        /**
         * @param rr the rr to set
         */
        public void setRr(OriginalListBean rr) {
            this.rr = rr;
        }

        /**
         * @return the newSalesNum
         */
        public OriginalListBean getNewSalesNum() {
            return newSalesNum;
        }

        /**
         * @param newSalesNum the newSalesNum to set
         */
        public void setNewSalesNum(OriginalListBean newSalesNum) {
            this.newSalesNum = newSalesNum;
        }

        /**
         * @return the fixedSalesNum
         */
        public OriginalListBean getFixedSalesNum() {
            return fixedSalesNum;
        }

        /**
         * @param fixedSalesNum the fixedSalesNum to set
         */
        public void setFixedSalesNum(OriginalListBean fixedSalesNum) {
            this.fixedSalesNum = fixedSalesNum;
        }

        /**
         * @return the toRate
         */
        public OriginalListBean getToRate() {
            return toRate;
        }

        /**
         * @param toRate the toRate to set
         */
        public void setToRate(OriginalListBean toRate) {
            this.toRate = toRate;
        }

        /**
         * @return the salesNum
         */
        public OriginalListBean getSalesNum() {
            return salesNum;
        }

        /**
         * @param salesNum the salesNum to set
         */
        public void setSalesNum(OriginalListBean salesNum) {
            this.salesNum = salesNum;
        }

        /**
         * @return the salesDetailNum
         */
        public OriginalListBean getSalesDetailNum() {
            return salesDetailNum;
        }

        /**
         * @param salesDetailNum the salesDetailNum to set
         */
        public void setSalesDetailNum(OriginalListBean salesDetailNum) {
            this.salesDetailNum = salesDetailNum;
        }

        /**
         * @return the designatedNum
         */
        public OriginalListBean getDesignatedNum() {
            return designatedNum;
        }

        /**
         * @param designatedNum the designatedNum to set
         */
        public void setDesignatedNum(OriginalListBean designatedNum) {
            this.designatedNum = designatedNum;
        }

        /**
         * @return the goodNum
         */
        public OriginalListBean getGoodNum() {
            return goodNum;
        }

        /**
         * @param goodNum the goodNum to set
         */
        public void setGoodNum(OriginalListBean goodNum) {
            this.goodNum = goodNum;
        }

        /**
         * @return the badNum
         */
        public OriginalListBean getBadNum() {
            return badNum;
        }

        /**
         * @param badNum the badNum to set
         */
        public void setBadNum(OriginalListBean badNum) {
            this.badNum = badNum;
        }

        /**
         * @return the retouchNum
         */
        public OriginalListBean getRetouchNum() {
            return retouchNum;
        }

        /**
         * @param retouchNum the retouchNum to set
         */
        public void setRetouchNum(OriginalListBean retouchNum) {
            this.retouchNum = retouchNum;
        }
        
        /**
         * @return the retouchNumEachSlip
         */
        public OriginalListBean getRetouchNumEachSlip() {
            return retouchNumEachSlip;
        }

        /**
         * @param retouchNumEachSlip the retouchNumEachSlip to set
         */
        public void setRetouchNumEachSlip(OriginalListBean retouchNumEachSlip) {
            this.retouchNumEachSlip = retouchNumEachSlip;
        }

        /**
         * @return the retouchRate
         */
        public OriginalListBean getRetouchRate() {
            return retouchRate;
        }

        /**
         * @param retouchRate the retouchRate to set
         */
        public void setRetouchRate(OriginalListBean retouchRate) {
            this.retouchRate = retouchRate;
        }

        /**
         * @return the productNum
         */
        public OriginalListBean getProductNum() {
            return productNum;
        }

        /**
         * @param productNum the productNum to set
         */
        public void setProductNum(OriginalListBean productNum) {
            this.productNum = productNum;
        }

        /**
         * @return the proceedsNum
         */
        public OriginalListBean getProceedsNum() {
            return proceedsNum;
        }

        /**
         * @param proceedsNum the proceedsNum to set
         */
        public void setProceedsNum(OriginalListBean proceedsNum) {
            this.proceedsNum = proceedsNum;
        }

        public void addBean(OriginalReport01DetailBean bean) {
            if (this.badNum == null) {
                this.badNum = new OriginalListBean();
            }
            this.badNum.addBean(bean.getBadNum());
            
            if (this.designatedNum == null) {
                this.designatedNum = new OriginalListBean();
            }
            this.designatedNum.addBean(bean.getDesignatedNum());
            
            if (this.goodNum == null) {
                this.goodNum = new OriginalListBean();
            }
            this.goodNum.addBean(bean.getGoodNum());
            
            if (this.proceedsNum == null) {
                this.proceedsNum = new OriginalListBean();
            }
            this.proceedsNum.addBean(bean.getProceedsNum());
            
            if (this.productNum == null) {
                this.productNum = new OriginalListBean();
            }
            this.productNum.addBean(bean.getProductNum());
            
            if (this.retouchNum == null) {
                this.retouchNum = new OriginalListBean();
            }
            this.retouchNum.addBean(bean.getRetouchNum());
            
            if (this.retouchNumEachSlip == null) {
                this.retouchNumEachSlip = new OriginalListBean();
            }
            this.retouchNumEachSlip.addBean(bean.getRetouchNumEachSlip());
            
            if (this.retouchRate == null) {
                this.retouchRate = new OriginalListBean();
            }
            this.retouchRate.addBean(bean.getRetouchRate());
            
            if (this.salesNum == null) {
                this.salesNum = new OriginalListBean();
            }
            this.salesNum.addBean(bean.getSalesNum());
            
            if (this.salesDetailNum == null) {
                this.salesDetailNum = new OriginalListBean();
            }
            this.salesDetailNum.addBean(bean.getSalesNum());
                        
            if (this.r2 == null) {
                this.r2 = new OriginalListBean();
            }
            this.r2.addBean(bean.getR2());
                        
            if (this.rr == null) {
                this.rr = new OriginalListBean();
            }
            this.rr.addBean(bean.getRr());
            
            if (this.newSalesNum == null) {
                this.newSalesNum = new OriginalListBean();
            }
            this.newSalesNum.addBean(bean.getNewSalesNum());
            
            if (this.fixedSalesNum == null) {
                this.fixedSalesNum = new OriginalListBean();
            }
            this.fixedSalesNum.addBean(bean.getFixedSalesNum());

            if (this.toRate == null) {
                this.toRate = new OriginalListBean();
            }
            this.toRate.addBean(bean.getToRate());
        }

    }
    
    public OriginalReport01Logic(OriginalReportBean bean) {
        super(bean);
    }

    @Override
    protected String getReportName() {
        switch (this.getBean().getExpType()) {
            case RANKING:
                return "ランキング";
            case MONTH_TRANSITION:
                return "月次推移";
            default:
                break;
        }
        return null;
    }

    @Override
    protected Result process(ConnectionWrapper connection) throws Exception {

        //ヘッダーの書き込み
        switch (this.getBean().getExpType()) {
            case RANKING:
                this.writeHeaderRanking();
                break;
            default:
                break;
        }
        
        // 明細の元情報を取得
        Map<Integer, String> baseMap = this.getBaseRankingList(connection);

        // 売上取得処理、TO数取得処理は時間がかかるため、対象店舗・スタッフのデータをあらかじめすべて取得しておき、
    	// ループ内では取得を行わない
        List<Integer> shopIdList = new ArrayList<>();
        List<Integer> staffIdList = new ArrayList<>();
        List<Integer> idList;
        if (super.getBean().isShopFlag()) {
            idList = shopIdList;
        } else {
            idList = staffIdList;
            if (OriginalReportBean.ExpReportType.RANKING.equals(this.getBean().getExpType())) {
                Integer shopId = this.getBean().getShopId();
                if(shopId != null) {
                    shopIdList.add(shopId);
                }
            }
        }
        for(Integer key : baseMap.keySet()) {
            idList.add(key);
        }
        Map<Integer, Map<String, Long>> productNumMap = getProductNum(connection, shopIdList, staffIdList);
        Map<Integer, Map<String, Long>> toCountMap = getToCount(connection, shopIdList, staffIdList);

        if (!baseMap.isEmpty()) {
            for (Integer key : baseMap.keySet()) {
                OriginalReport01Bean row = new OriginalReport01Bean();
                if (super.getBean().isShopFlag()) {
                    row.setShopId(key);
                    row.setShopName(baseMap.get(key));
                } else if (super.getBean().isStaffFlag()) {
                    row.setStaffId(key);
                    row.setStaffName(baseMap.get(key));
                    
                    if (OriginalReportBean.ExpReportType.RANKING.equals(this.getBean().getExpType())) {
                        row.setShopId(this.getBean().getShopId());
                    }
                }
                
                // 客数、指名数の設定
                this.setSalesNumAll(connection, row);
                this.setDesignatedNum(connection, row);
                // R2、RRの設定
//                long start = System.currentTimeMillis();
                this.setSalesNumRR(connection, row);
//                System.out.println(System.currentTimeMillis() - start);
                // 口コミ数の設定
                this.setEvaluationNum(connection, row);
                // お直し数の設定
                this.setRetouchNum(connection, row);
                // 売上、単価の設定
                this.setProductNum(productNumMap, row);
                // TO率
                this.setToRateNum(toCountMap, row);
                
                this.reportList.add(row);
            }
        }
        
        switch (this.getBean().getExpType()) {
            case RANKING:
                this.writeMainForRanking();
                break;
            case MONTH_TRANSITION:
                this.writeMainForMonthTransition();
                break;
            default:
                break;
        }
        
        return Result.PRINT;
    }
    
    /**
     * 月次推移用ヘッダー
     * @param sheetName 
     */
    protected void writeHeader(String sheetName) {
        if (this.getBean().isShopFlag() ) {
            //店舗の場合
            this.writeShopName(2, 3);
            this.writeValue(2, 4, "-");
        } else if (this.getBean().isStaffFlag() ) {
            //従業員の場合
            this.writeShopName(2, 3);
            
            String staffName;
            if (this.getBean().getStaffId() == null) {
                staffName = "全体";
            } else {
                staffName = this.getBean().getStaffName();
            }
            this.writeValue(2, 4, staffName);
        }
        if (OUTPUT_PERIOD_COLUMNS[0].equals(sheetName) || OUTPUT_PERIOD_COLUMNS[1].equals(sheetName)) {
            super.writeVerticalItems(2, 5, WriteItem.TARGET_RANGE);
            super.writeVerticalItems(2, 6, WriteItem.PERIOD_RANGE);
            super.writeValue(2, 7, COLUMN_OUTPUT_MAP.get(sheetName));
            super.writeVerticalItems(2, 8, WriteItem.TAX);
        } else {
            super.writeVerticalItems(2, 5, WriteItem.TARGET_RANGE);
            super.writeValue(2, 6, sheetName);
            super.writeVerticalItems(2, 7, WriteItem.TAX);
        }
    }
    
    /**
     * ランキング用ヘッダー
     */
    protected void writeHeaderRanking() {
        if (this.getBean().isShopFlag() ) {
            //店舗の場合
            //店舗には、全店を表記
            this.writeValue(2, 3, "全店");
            this.writeValue(2, 4, "-");
        } else if (this.getBean().isStaffFlag() ) {
            //従業員の場合
            this.writeShopName(2, 3);
            this.writeValue(2, 4, "全体");
        }
        super.writeVerticalItems(2, 5
                , WriteItem.TARGET_RANGE
                , WriteItem.PERIOD_RANGE
                , WriteItem.ORDER
                , WriteItem.TAX);
    }
    
    /**
     * ランキング用メイン出力処理
     * @throws java.lang.Exception
     */
    protected void writeMainForRanking() throws Exception {
        String listHeaderName = null;
        if (super.getBean().isShopFlag()) {
            listHeaderName = "店舗名";
        }
        if (super.getBean().isStaffFlag()) {
            listHeaderName = "スタッフ名";
        }
        int rowHeader = 10;
        super.writeValue(1, rowHeader, listHeaderName);
        
        int rowIndex = rowHeader + 1;
        if (!this.reportList.isEmpty()) {
            
            int addRow = this.reportList.size() - 2;
            this.getReport().insertRow(rowIndex, addRow);
            
            List<OriginalReport01DetailBean> summaryList = this.toSummaryList();
            
            for (OriginalReport01DetailBean detail : summaryList) {
                
                // 店舗名 or スタッフ名
                super.writeValue(1, rowIndex, detail.getName());
                
                // R2（新規再来）
                super.writeValue(2, rowIndex, nullToZero(detail.getR2().getRate()));
                
                // R全（新規・再来含）
                super.writeValue(3, rowIndex, nullToZero(detail.getRr().getRate()));
                
                // TO率（時間超過率）
                super.writeValue(4, rowIndex, nullToZero(detail.getToRate().getRate()));
                
                // 客数
                super.writeValue(5, rowIndex, nullToZero(detail.getSalesNum().getNum()));
                
                // 指名数
                super.writeValue(6, rowIndex, nullToZero(detail.getDesignatedNum().getNum()));
                
                // 口コミ数（良い）
                super.writeValue(7, rowIndex, nullToZero(detail.getGoodNum().getNum()));
                
                // 口コミ数（悪い）
                super.writeValue(8, rowIndex, nullToZero(detail.getBadNum().getNum()));
                
                // お直し数、お直し率
                super.setRate(detail.getRetouchNumEachSlip(), detail.getSalesNum().getNum());
                super.writeValue(9, rowIndex, nullToZero(detail.getRetouchNum().getNum()));
                super.writeValue(10, rowIndex, nullToZero(detail.getRetouchNumEachSlip().getRate()));
                
                // 単価
                super.writeValue(11, rowIndex, nullToZero(detail.getProductNum().getNum()));
                
                // 売上
                super.writeValue(12, rowIndex, nullToZero(detail.getProceedsNum().getNum()));
                
                rowIndex++;
            }
        }
    }

    private List<OriginalReport01DetailBean> toSummaryList() {
        //リストの再構築
        java.util.List<OriginalReport01DetailBean> summaryDetailList = new ArrayList<>();
        for (OriginalReport01Bean row : this.reportList) {
            OriginalReport01DetailBean detail = row.getSummaryDetail();
            
            
//            if (this.getBean().isTargetPeriod02Flag()) {
//                // fromto選択時はサマリではなく最後の年月のデータをもとに再来率を算出するようにする。
//                // fromto選択時は最後の年月のデータがトータルの値になっているため。
            // ランキング選択時は最後の年月のデータをもとに再来率を算出するようにする。
            Map<String, OriginalReport01DetailBean> d = row.getDetail();
            String lastYm = "";
            for (String key : d.keySet()) {
                if (key.compareTo(lastYm) > 0) {
                    lastYm = key;
                }
            }
            detail.getR2().setNum(d.get(lastYm).getR2().getNum());
            detail.getRr().setNum(d.get(lastYm).getRr().getNum());
            detail.getNewSalesNum().setNum(d.get(lastYm).getNewSalesNum().getNum());
            detail.getFixedSalesNum().setNum(d.get(lastYm).getFixedSalesNum().getNum());
//            }
            
            // R2（新規再来）
            super.setRate(detail.getR2(), detail.getNewSalesNum().getNum());
            // R全（新規・再来含）
            super.setRate(detail.getRr(), detail.getFixedSalesNum().getNum());
            // TO率（時間超過率）
            super.setRate(detail.getToRate(), detail.getSalesNum().getNum());
            // お直し数、お直し率
            super.setRate(detail.getRetouchNumEachSlip(), detail.getSalesNum().getNum());
            // 単価
            detail.setProductNum(new OriginalListBean(null
                    , calcUnitPrice(detail.getProceedsNum().getNum(), detail.getSalesNum().getNum())));
            
            // 店舗名 or スタッフ名
            String nameVal = null;
            if (super.getBean().isShopFlag()) {
                nameVal = row.getShopName();
            }
            if (super.getBean().isStaffFlag()) {
                nameVal = row.getStaffName();
            }

            detail.setName(nameVal);

            summaryDetailList.add(detail);
        }
        
        //集計結果リストのソート処理
        sortByOrder(summaryDetailList);
        
        return summaryDetailList;
    }

    private void sortByOrder(List<OriginalReport01DetailBean> summaryDetailList) {
        //ソート対象項目
        int orderIndex = this.getBean().getOrderIndex();
        OriginalReportBean.OrderType selected = null;
        for ( OriginalReportBean.OrderType o : OriginalReportBean.OrderType.values() ) {
            if ( o.ordinal() == orderIndex ) {
                selected = o;
                break;
            }
        }
        switch(selected) {
        case R2:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o2.getR2().getRate()).compareTo(nullToZero(o1.getR2().getRate()));
                }
            });
            break;
        case RR:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o2.getRr().getRate()).compareTo(nullToZero(o1.getRr().getRate()));
                }
            });
            break;
        case TO_RATE:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o1.getToRate().getRate()).compareTo(nullToZero(o2.getToRate().getRate()));
                }
            });
            break;
        case SALES:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o2.getSalesNum().getNum()).compareTo(nullToZero(o1.getSalesNum().getNum()));
                }
            });
            break;
        case DESIGNATED:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o2.getDesignatedNum().getNum()).compareTo(nullToZero(o1.getDesignatedNum().getNum()));
                }
            });
            break;
        case GOOD:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o2.getGoodNum().getNum()).compareTo(nullToZero(o1.getGoodNum().getNum()));
                }
            });
            break;
        case BAD:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o1.getBadNum().getNum()).compareTo(nullToZero(o2.getBadNum().getNum()));
                }
            });
            break;
        case RETOUCH:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o1.getRetouchNum().getNum()).compareTo(nullToZero(o2.getRetouchNum().getNum()));
                }
            });
            break;
        case RETOUCH_RATE:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o1.getRetouchNumEachSlip().getRate()).compareTo(nullToZero(o2.getRetouchNumEachSlip().getRate()));
                }
            });
            break;
        case PRODUCT:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o2.getProductNum().getNum()).compareTo(nullToZero(o1.getProductNum().getNum()));
                }
            });
            break;
        case PROCEEDS:
            Collections.sort(summaryDetailList, new Comparator<OriginalReport01DetailBean>() {
                @Override
                public int compare(OriginalReport01DetailBean o1, OriginalReport01DetailBean o2) {
                    return nullToZero(o2.getProceedsNum().getNum()).compareTo(nullToZero(o1.getProceedsNum().getNum()));
                }
            });
            break;
            
        }
    }
    
    private Long calcUnitPrice(Long proceedsNum, Long salseNum) {
        Long price = 0L;
        
        if ( proceedsNum == null ) {
            return price;
        }
        
        if ( salseNum != null && salseNum != 0L ) {
            price = proceedsNum / salseNum;
        }
        return price;
    }
    
    private Long nullToZero(Long value) {
        return value == null ? 0L : value; 
    }
    
    private Double nullToZero(Double value) {
        return value == null ? 0L : value; 
    }
    
    protected void writeMainForMonthTransition() throws Exception {
        // 再来算出期間対象の出力を行う
        this.writeMainForMonthTransitionDetail(this.getTargetYmList(), OUTPUT_PERIOD_COLUMNS);
        // 対象期間対象の出力を行う
        this.writeMainForMonthTransitionDetail(this.getTargetYmList(), OUTPUT_TARGET_COLUMNS);
        
    }
    
    protected void writeMainForMonthTransitionDetail(
            List<String> ymList, String... columns) throws Exception {
        
        String listHeaderName = null;
        if (super.getBean().isShopFlag()) {
            listHeaderName = "店舗名";
        }
        if (super.getBean().isStaffFlag()) {
            listHeaderName = "スタッフ名";
        }
        
        for (String column : columns) {
            super.getReport().setTargetSheet(column);

            int columnIndex = 2;
            if (!ymList.isEmpty()) {
                int addColumn = ymList.size() - 2;
                this.getReport().insertColumn(columnIndex, addColumn);
            }

            int rowIndex = 28;

            if (!this.reportList.isEmpty()) {

                int addRow = this.reportList.size() - 2;
                this.getReport().insertRow(rowIndex, addRow);
            }
                        
            this.writeHeader(column);
            
            super.writeValue(1, rowIndex - 1, listHeaderName);
        }
        
        if (!ymList.isEmpty()) {
            Map<Integer, String> nameMap = new LinkedHashMap<>();
            
            for (int i = 0; i < ymList.size(); i++) {
                
                if (!this.reportList.isEmpty()) {
                    Map<String, List<OriginalListBean>> columnsDetail = new LinkedHashMap<>();
                    
                    for (OriginalReport01Bean row : this.reportList) {
                        // 店舗名 or スタッフ名
                        String nameVal = null;
                        Integer id = null;
                        if (super.getBean().isShopFlag()) {
                            nameVal = row.getShopName();
                            id = row.getShopId();
                        }
                        if (super.getBean().isStaffFlag()) {
                            nameVal = row.getStaffName();
                            id = row.getStaffId();
                        }
                        nameMap.put(id, nameVal);
                        
                        Map<String, OriginalReport01DetailBean> detail = row.getDetail();
                        
                        if (detail.containsKey(ymList.get(i))) {
                            this.addColumnsDetail(columnsDetail, detail.get(ymList.get(i)));
                        } else {
                            this.addColumnsDetail(columnsDetail, new OriginalReport01DetailBean());
                        }
                    }
                    List<OriginalListBean> salesNums = columnsDetail.get(OUTPUT_TARGET_COLUMNS[1]);
                    
                    for (String column : columns) {
                        super.getReport().setTargetSheet(column);
                        int rowTitle = 27;
                        
                        super.writeValue(2 + i, rowTitle, ymList.get(i));
                        List<OriginalListBean> beans = columnsDetail.get(column);
                        
                        int index = 0;
                        int rowIndex = 28;
                        
                        for (OriginalListBean bean : beans) {
                            if (column.equals(OUTPUT_TARGET_COLUMNS[0])
                             || column.equals(OUTPUT_TARGET_COLUMNS[6])) {
                                super.setRate(bean, salesNums.get(index++).getNum());
                                super.writeValue(2 + i, rowIndex++
                                    , bean.getRate() == null ? 0.0 : bean.getRate());
                                
                            } else if (column.equals(OUTPUT_PERIOD_COLUMNS[0])) {
                                // R2（新規再来）の場合は新規顧客数で算出する。
                                List<OriginalListBean> allNum = columnsDetail.get(OUTPUT_OTHER_COLUMNS[0]);
                                
                                super.setRate(bean, allNum.get(index++).getNum());
                                super.writeValue(2 + i, rowIndex++
                                    , bean.getRate() == null ? 0.0 : bean.getRate());
                                
                            } else if (column.equals(OUTPUT_PERIOD_COLUMNS[1])) {
                                // R（既存再来）の場合は既存顧客数で算出する。
                                List<OriginalListBean> allNum = columnsDetail.get(OUTPUT_OTHER_COLUMNS[1]);
                                
                                super.setRate(bean, allNum.get(index++).getNum());
                                super.writeValue(2 + i, rowIndex++
                                    , bean.getRate() == null ? 0.0 : bean.getRate());
                                
                            } else {
                                super.writeValue(2 + i, rowIndex++
                                    , bean.getNum() == null ? 0 : bean.getNum());
                            }
                        }
                    }
                }
            }
            if (!nameMap.isEmpty()) {
                for (String column : columns) {
                    super.getReport().setTargetSheet(column);
                    int rowIndex = 28;

                    for (Integer id : nameMap.keySet()) {
                        super.writeValue(1, rowIndex++, nameMap.get(id));
                    }
                }
            }
        }
    }
    
    private void addColumnsDetail(Map<String, List<OriginalListBean>> columnsDetail, OriginalReport01DetailBean row) {
        
        this.addColumnsDetail(columnsDetail, row.getR2(), OUTPUT_PERIOD_COLUMNS[0]);
        this.addColumnsDetail(columnsDetail, row.getRr(), OUTPUT_PERIOD_COLUMNS[1]);
        
        this.addColumnsDetail(columnsDetail, row.getToRate(), OUTPUT_TARGET_COLUMNS[0]);
        this.addColumnsDetail(columnsDetail, row.getSalesNum(), OUTPUT_TARGET_COLUMNS[1]);
        this.addColumnsDetail(columnsDetail, row.getDesignatedNum(), OUTPUT_TARGET_COLUMNS[2]);
        this.addColumnsDetail(columnsDetail, row.getGoodNum(), OUTPUT_TARGET_COLUMNS[3]);
        this.addColumnsDetail(columnsDetail, row.getBadNum(), OUTPUT_TARGET_COLUMNS[4]);
        this.addColumnsDetail(columnsDetail, row.getRetouchNum(), OUTPUT_TARGET_COLUMNS[5]);
        this.addColumnsDetail(columnsDetail, row.getRetouchNumEachSlip(), OUTPUT_TARGET_COLUMNS[6]);
        this.addColumnsDetail(columnsDetail, row.getProductNum(), OUTPUT_TARGET_COLUMNS[7]);
        this.addColumnsDetail(columnsDetail, row.getProceedsNum(), OUTPUT_TARGET_COLUMNS[8]);
        
        this.addColumnsDetail(columnsDetail, row.getNewSalesNum(), OUTPUT_OTHER_COLUMNS[0]);
        this.addColumnsDetail(columnsDetail, row.getFixedSalesNum(), OUTPUT_OTHER_COLUMNS[1]);
    }
    
    private void addColumnsDetail(
            Map<String, List<OriginalListBean>> columnsDetail, OriginalListBean bean, String column) {
        if (!columnsDetail.containsKey(column)) {
            columnsDetail.put(column, new ArrayList<OriginalListBean>());
        }
        columnsDetail.get(column).add(bean == null ? new OriginalListBean(null, 0L) : bean);
    }
    
    private List<String> getTargetYmList() {
        return this.getYmRangeList(
                super.getBean().getTargetDateFrom()
                , super.getBean().getTargetDateTo());
    }
    
    private List<String> getPeriodYmList() {
        return this.getYmRangeList(
                super.getBean().getPeriodDateFrom()
                , super.getBean().getPeriodDateTo());
    }
    
    private List<String> getYmRangeList(Date fromDate, Date toDate) {
        List<String> returnList = new ArrayList<>();
        
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.setTime(fromDate);
        to.setTime(toDate);
        to.set(Calendar.DATE, from.get(Calendar.DATE));
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
        while (from.compareTo(to) <= 0) {
            returnList.add(format.format(from.getTime()));
            from.add(Calendar.MONTH, 1);
        }
        return returnList;
    }
    
    /**
     * 明細の元となる情報を取得します。
     * 
     * @param conn DBコネクション
     * @return 明細の元となる情報（店舗 or スタッフ）
     * @throws Exception 汎用例外
     */
    private Map<Integer, String> getBaseList(ConnectionWrapper conn) throws Exception {
        
        StringBuilder query = new StringBuilder();
        query.append(" SELECT DISTINCT");
        
        if (super.getBean().isShopFlag()) {
            query.append("   t1.shop_id AS sales_id");
            query.append("   , m1.shop_name AS sales_name");
            query.append(" FROM");
            query.append("   data_sales AS t1");
            query.append(" LEFT OUTER JOIN");
            query.append("   mst_shop AS m1");
            query.append("   ON (t1.shop_id = m1.shop_id)");
            
        } else if (super.getBean().isStaffFlag()) {
            query.append("   t2.staff_id AS sales_id");
            query.append("   , m1.staff_name1 || m1.staff_name2 AS sales_name");
            query.append(" FROM");
            query.append("   data_sales AS t1");
            query.append(" INNER JOIN");
            query.append("   data_sales_detail AS t2");
            query.append("   ON(t1.shop_id = t2.shop_id AND t1.slip_no = t2.slip_no)");
            query.append(" LEFT OUTER JOIN");
            query.append("   mst_staff AS m1");
            query.append("   ON (t1.staff_id = m1.staff_id)");
        }
        
        query.append(" WHERE");
        query.append("   t1.delete_date IS NULL");
        
        if (super.getBean().getShopId() != null) {
            query.append("   AND t1.shop_id = ").append(SQLUtil.convertForSQL(super.getBean().getShopId()));
        }
        if (super.getBean().getStaffId() != null) {
            query.append("   AND t2.staff_id = ").append(SQLUtil.convertForSQL(super.getBean().getStaffId()));
        }
        if (super.getBean().getTargetDateFrom() != null) {
            query.append("   AND t1.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        }
        if (super.getBean().getTargetDateTo() != null) {
            query.append("   AND t1.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        }

        //SQL実行
        ResultSetWrapper rs = conn.executeQuery(query.toString());
        rs.beforeFirst();

        Map<Integer, String> resultMap = new LinkedHashMap<>();
        while (rs.next()) {
            resultMap.put(rs.getInt("sales_id"), rs.getString("sales_name"));
        }
        
        return resultMap;
    }
    
    /**
     * ランキング用の明細の元となる情報を取得します。
     * 
     * @param conn DBコネクション
     * @return 明細の元となる情報（店舗 or スタッフ）
     * @throws Exception 汎用例外
     */
    private Map<Integer, String> getBaseRankingList(ConnectionWrapper conn) throws Exception {
        Map<Integer, String> resultMap = new LinkedHashMap<>();
        
        if (super.getBean().isShopFlag()) {
            //店舗の場合は、全店舗取得
            ArrayList<MstShop> list = SystemInfo.getGroup().getShops();
            for (MstShop mst: list) {
                if (this.getBean().getShopId() == null ||
                    OriginalReportBean.ExpReportType.RANKING.equals(this.getBean().getExpType())) {
                    //店舗IDがない場合、ランキングの場合は、全店舗を出力対象とする
                    resultMap.put(mst.getShopID(), mst.getShopName());
                } else if (this.getBean().getShopId().equals(mst.getShopID())) {
                    resultMap.put(mst.getShopID(), mst.getShopName());
                }
            }
        } else if (super.getBean().isStaffFlag()) {

            //出力区分：担当者の場合
            MstStaffs staffs = new MstStaffs();
            if (this.getBean().getShopId() == null) {
                //店舗IDがない場合は、全従業員取得
                staffs.setShopIDList("1");
                staffs.load(conn, false);
            } else {
                //店舗IDがある場合は、店舗の従業員全員取得
                staffs.setShopIDList(this.getBean().getShopId().toString());
                staffs.loadOnlyShop(conn, false);
            }
            for (MstStaff mst: staffs) {
                if (this.getBean().getStaffId() == null) {
                    // 担当者未選択の場合は全担当者が出力対象となる。
                    resultMap.put(mst.getStaffID(), mst.toString());
                } else if (this.getBean().getStaffId().equals(mst.getStaffID())) {
                    resultMap.put(mst.getStaffID(), mst.toString());
                }
            }
        }
      
        return resultMap;
    }
    
    /**
     * 客数の設定を行います。
     * 
     * @param conn DBコネクション
     * @param row 明細情報
     * @throws Exception 汎用例外
     */
    private void setSalesNumAll(
            ConnectionWrapper conn, OriginalReport01Bean row) throws Exception {
        
        StringBuilder query = new StringBuilder();
        query.append(" SELECT");
        query.append("   sub.sales_ym");
        query.append("   , COUNT(sub.*) AS sales_num_all");
        query.append("   , SUM(sub.detail_num) AS detail_num_all");
        query.append(" FROM (");
        query.append("   SELECT");
        query.append("     t1.slip_no");
        query.append("     , TO_CHAR(t1.sales_date, 'YYYY/MM') AS sales_ym");
        query.append("     , COUNT(*) AS detail_num");
        query.append("   FROM");
        query.append("     data_sales AS t1");
        query.append("   INNER JOIN");
        query.append("     data_sales_detail AS t2");
        query.append("     ON(t1.shop_id = t2.shop_id AND t1.slip_no = t2.slip_no)");
        query.append("   WHERE");
        query.append("     t2.delete_date IS NULL");
        
        if (row.getShopId() != null) {
            query.append("   AND t1.shop_id = ").append(SQLUtil.convertForSQL(row.getShopId()));
        }
        if (row.getStaffId() != null) {
            query.append("   AND (t1.staff_id = ").append(SQLUtil.convertForSQL(row.getStaffId()));
            query.append("        OR t2.staff_id = ").append(SQLUtil.convertForSQL(row.getStaffId())).append(") ");
        }
        if (super.getBean().getTargetDateFrom() != null) {
            query.append("   AND t1.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        }
        if (super.getBean().getTargetDateTo() != null) {
            query.append("   AND t1.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        }
        
        query.append("   GROUP BY t1.slip_no, t1.sales_date");
        
        query.append(" ) AS sub");
        query.append(" GROUP BY");
        query.append("   sub.sales_ym");
        query.append(" ORDER BY");
        query.append("   sub.sales_ym");
        
        //SQL実行
        ResultSetWrapper rs = conn.executeQuery(query.toString());
        rs.beforeFirst();

        while (rs.next()) {
            String salesYm = rs.getString("sales_ym");
            if (!row.getDetail().containsKey(salesYm)) {
                row.getDetail().put(salesYm, new OriginalReport01DetailBean());
            }
            // 客数
            row.getDetail().get(salesYm).setSalesNum(
                    new OriginalListBean(null, rs.getLong("sales_num_all")));
            
            // 施術数
            row.getDetail().get(salesYm).setSalesDetailNum(
                    new OriginalListBean(null, rs.getLong("detail_num_all")));
            
            // 指名数
//            row.getDetail().get(salesYm).setDesignatedNum(
//                    new OriginalListBean(null, rs.getLong("designated_num")));
        }
    }
    
    /**
     * 指名数の設定を行います。
     * 
     * @param conn DBコネクション
     * @param row 明細情報
     * @throws Exception 汎用例外
     */
    private void setDesignatedNum(
            ConnectionWrapper conn, OriginalReport01Bean row) throws Exception {
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append("  designate_data.sales_ym");
	sb.append("  , COUNT(*) AS designated_num ");
	sb.append("FROM ");
	sb.append("  ( ");
	sb.append("    SELECT DISTINCT ");
        sb.append("      TO_CHAR(a.sales_date, 'YYYY/MM') AS sales_ym");
	sb.append("      , a.shop_id ");
	sb.append("      , a.slip_no ");
	sb.append("    FROM ");
	sb.append("      data_sales a JOIN data_sales_detail b ");
	sb.append("        ON a.shop_id = b.shop_id ");
	sb.append("        AND a.slip_no = b.slip_no ");
	sb.append("    WHERE ");
        sb.append("      b.delete_date IS NULL ");
	if (row.getShopId() != null) {
            sb.append("   AND a.shop_id = ").append(SQLUtil.convertForSQL(row.getShopId()));
        }
        if (row.getStaffId() != null) {
            sb.append("   AND ( ");
            sb.append("     (b.designated_flag = true AND b.staff_id = ").append(SQLUtil.convertForSQL(row.getStaffId())).append(") ");
            sb.append("     OR (a.designated_flag = true AND a.staff_id = ").append(SQLUtil.convertForSQL(row.getStaffId())).append(") ");
            sb.append("   ) ");
        } else {
            sb.append("   AND ( ");
            sb.append("     b.designated_flag = true ");
            sb.append("     OR a.designated_flag = true ");
            sb.append("   ) ");
        }
        if (super.getBean().getTargetDateFrom() != null) {
            sb.append("   AND a.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        }
        if (super.getBean().getTargetDateTo() != null) {
            sb.append("   AND a.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        }
	sb.append("  ) AS designate_data ");
        sb.append(" GROUP BY");
        sb.append("   designate_data.sales_ym");
        sb.append(" ORDER BY");
        sb.append("   designate_data.sales_ym");
        
        //SQL実行
        ResultSetWrapper rs = conn.executeQuery(sb.toString());

        while (rs.next()) {
            String salesYm = rs.getString("sales_ym");
            if (!row.getDetail().containsKey(salesYm)) {
                row.getDetail().put(salesYm, new OriginalReport01DetailBean());
            }
            // 指名数
            row.getDetail().get(salesYm).setDesignatedNum(
                    new OriginalListBean(null, rs.getLong("designated_num")));
        }
    }
    
    /**
     * R2（新規再来）、R全（新規・再来含）を設定します。
     * 
     * @param conn DBコネクション
     * @param row 明細情報
     * @throws Exception 汎用例外
     */
    private void setSalesNumRR(
            ConnectionWrapper conn, OriginalReport01Bean row) throws Exception {
        
        Map<String, List<Date>> periodYmdMap = super.getBean().getPeriodYmdMap();
        Iterator<String> targetYmIterator = periodYmdMap.keySet().iterator();
        Calendar targetDate = Calendar.getInstance();
        
        while (targetYmIterator.hasNext()) {
            String targetYm = targetYmIterator.next();
            
            int targetYear = Integer.valueOf(targetYm.substring(0, 4));
            int targetMonth = Integer.valueOf(targetYm.substring(targetYm.length() - 2));
            
            targetDate.set(Calendar.YEAR, targetYear);
            targetDate.set(Calendar.MONTH, targetMonth - 1);
            targetDate.set(Calendar.DAY_OF_MONTH, 1);
            targetDate.set(Calendar.DAY_OF_MONTH, targetDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date targetEndDate = targetDate.getTime();
            
            Date periodStartDate = periodYmdMap.get(targetYm).get(0);
            Date periodEndDate = periodYmdMap.get(targetYm).get(1);

            // ※対象期間開始日は再来算出期間開始日と同じ
            ResultSetWrapper rs = 
                    conn.executeQuery(this.getRrR2Sql(row, periodStartDate, targetEndDate, periodStartDate, periodEndDate));

            while (rs.next()) {
                if (!row.getDetail().containsKey(targetYm)) {
                    row.getDetail().put(targetYm, new OriginalReport01DetailBean());
                }
                // R2（新規再来）
                row.getDetail().get(targetYm).setR2(
                        new OriginalListBean(null, rs.getLong("r2_num")));
                // R全（新規・再来含）
                row.getDetail().get(targetYm).setRr(
                        new OriginalListBean(null, rs.getLong("rr_num")));

                // 新規顧客数
                row.getDetail().get(targetYm).setNewSalesNum(
                        new OriginalListBean(null, rs.getLong("all_new_num")));
                // 既存顧客数
                row.getDetail().get(targetYm).setFixedSalesNum(
                        new OriginalListBean(null, rs.getLong("all_fixed_num")));
            }
            rs.close();
        }
    }
    
    /**
     * 口コミ数を設定します。
     * 
     * @param conn DBコネクション
     * @param row 明細情報
     * @throws Exception 汎用例外
     */
    private void setEvaluationNum(
            ConnectionWrapper conn, OriginalReport01Bean row) throws Exception {
        
        StringBuilder query = new StringBuilder();
        query.append(" SELECT");
        query.append("   sub.memo_ym");
        query.append("   , COUNT(sub.is_good) AS good_num");
        query.append("   , COUNT(sub.is_bad) AS bad_num");
        query.append(" FROM (");
        query.append("   SELECT ");
        query.append("     TO_CHAR(t3.memo_date, 'YYYY/MM') AS memo_ym");
        query.append("     , CASE");
        query.append("       WHEN t3.evaluation = 1 THEN 1");
        query.append("       ELSE NULL");
        query.append("     END AS is_good");
        query.append("     , CASE");
        query.append("       WHEN t3.evaluation = 2 THEN 1");
        query.append("       ELSE NULL");
        query.append("     END AS is_bad");
        query.append("   FROM");
        query.append("     data_customer_memo AS t3");
        query.append("   WHERE");
        query.append("     t3.delete_date IS NULL");
        
        if (row.getShopId() != null) {
            String shopId = SQLUtil.convertForSQL(row.getShopId());
            query.append("   AND t3.shop_id = ").append(shopId);
        }
        if (row.getStaffId() != null) {
            String staffId = SQLUtil.convertForSQL(row.getStaffId());
            query.append("   AND t3.staff_id = ").append(staffId);
        }
        if (super.getBean().getTargetDateFrom() != null) {
            String targetDateFrom = SQLUtil.convertForSQL(super.getBean().getTargetDateFrom());
            query.append("   AND t3.memo_date >= ").append(targetDateFrom);
        }
        if (super.getBean().getTargetDateTo() != null) {
            String targetDateTo = SQLUtil.convertForSQL(super.getBean().getTargetDateTo());
            query.append("   AND t3.memo_date <= ").append(targetDateTo);
        }
        
        query.append(" ) AS sub");
        query.append(" GROUP BY");
        query.append("   sub.memo_ym");
        query.append(" ORDER BY");
        query.append("   sub.memo_ym");
        
        // SQL実行
        ResultSetWrapper rs = conn.executeQuery(query.toString());
        rs.beforeFirst();

        while (rs.next()) {
            String salesYm = rs.getString("memo_ym");
            if (!row.getDetail().containsKey(salesYm)) {
                row.getDetail().put(salesYm, new OriginalReport01DetailBean());
            }
            // 口コミ数（良い）
            row.getDetail().get(salesYm).setGoodNum(
                    new OriginalListBean(null, rs.getLong("good_num")));
            // 口コミ数（悪い）
            row.getDetail().get(salesYm).setBadNum(
                    new OriginalListBean(null, rs.getLong("bad_num")));
        }
        
    }

    /**
     * お直し数を設定します。
     * 
     * @param conn DBコネクション
     * @param row 明細情報
     * @throws Exception 汎用例外
     */
    private void setRetouchNum(
            ConnectionWrapper conn, OriginalReport01Bean row) throws Exception {
        
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
	sb.append("  retouch.sales_ym ");
	sb.append("  , COUNT(*) AS retouch_num_each_slip ");
	sb.append("  , SUM(retouch.retouch_num) AS retouch_num ");
	sb.append("FROM ");
	sb.append("  ( ");
	sb.append("    SELECT ");
	sb.append("      t1.slip_no ");
	sb.append("      , t1.shop_id ");
	sb.append("      , TO_CHAR(t1.sales_date, 'YYYY/MM') AS sales_ym ");
	sb.append("      , COUNT(*) AS retouch_num ");
	sb.append("    FROM ");
	sb.append("      data_sales AS t1 ");
	sb.append("      INNER JOIN data_sales_detail AS t2 ");
	sb.append("        ON ( ");
	sb.append("          t1.shop_id = t2.shop_id ");
	sb.append("          AND t1.slip_no = t2.slip_no ");
	sb.append("        ) ");
	sb.append("    WHERE ");
	sb.append("      t2.delete_date IS NULL ");
	sb.append("      AND t2.product_division = 3 ");
	if (row.getShopId() != null) {
            sb.append("   AND t1.shop_id = ").append(SQLUtil.convertForSQL(row.getShopId()));
        }
        if (row.getStaffId() != null) {
            sb.append("   AND t2.staff_id = ").append(SQLUtil.convertForSQL(row.getStaffId()));
        }
        if (super.getBean().getTargetDateFrom() != null) {
            sb.append("   AND t1.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        }
        if (super.getBean().getTargetDateTo() != null) {
            sb.append("   AND t1.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        }
	sb.append("    GROUP BY ");
	sb.append("      t1.slip_no ");
	sb.append("      , t1.shop_id ");
	sb.append("      , sales_ym ");
	sb.append("    ORDER BY ");
	sb.append("      sales_ym ");
	sb.append("  ) retouch ");
	sb.append("GROUP BY ");
	sb.append("  retouch.sales_ym ");
        
        // SQL実行
        ResultSetWrapper rs = conn.executeQuery(sb.toString());
        rs.beforeFirst();

        while (rs.next()) {
            String salesYm = rs.getString("sales_ym");
            if (!row.getDetail().containsKey(salesYm)) {
                row.getDetail().put(salesYm, new OriginalReport01DetailBean());
            }
            // お直し数(明細ごと)
            row.getDetail().get(salesYm).setRetouchNum(
                    new OriginalListBean(null, rs.getLong("retouch_num")));
            
            // お直し数(売上ごと)
            row.getDetail().get(salesYm).setRetouchNumEachSlip(
                    new OriginalListBean(null, rs.getLong("retouch_num_each_slip")));
        }
    }
    
    /**
     * 単価と売上を設定します。
     * 
     * @param conn DBコネクション
     * @param row 明細情報
     * @throws Exception 汎用例外
     */
    private void setProductNum(Map<Integer, Map<String, Long>> productNumMap, OriginalReport01Bean row) throws Exception {
        Integer id;
        if (super.getBean().isShopFlag()) {
            id = row.getShopId();
        } else {
            id = row.getStaffId();
        }
        Map<String, Long> productNumMapYm = productNumMap.get(id);
        if(productNumMapYm == null) {
            return;
        }
        for(String salesYm : productNumMapYm.keySet()) {

            if (!row.getDetail().containsKey(salesYm)) {
                row.getDetail().put(salesYm, new OriginalReport01DetailBean());
            }

            OriginalReport01DetailBean bean = row.getDetail().get(salesYm);
            // 売上
            bean.setProceedsNum(new OriginalListBean(null, productNumMapYm.get(salesYm)));

            if ( bean.getSalesNum() == null || bean.getSalesNum().getNum() == 0L ) {
                // 単価(売上/客数)
                bean.setProductNum(new OriginalListBean(null, 0L));
            } else {
                // 単価(売上/客数)
                bean.setProductNum(new OriginalListBean(null
                        , bean.getProceedsNum().getNum() / bean.getSalesNum().getNum()));
            }
        }
    }
    
    /**
     * スタッフID・店舗IDをキーに年月とその時の売上を持つマップを作成
     * 
     * @param conn DBコネクション
     * @param targetStaffList 検索対象スタッフIDのリスト
     * @param targetShopList 検索対象店舗IDのリスト
     * @return 人・店舗をキーに年月とその時の売上数を持つマップ
     * @throws Exception 汎用例外
     */
    private Map<Integer, Map<String, Long>> getProductNum(
            ConnectionWrapper conn, List<Integer> targetShopList, List<Integer> targetStaffList) throws Exception {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT");
        query.append("   sub.sales_ym");
        query.append("   , sub.staff_id");
        query.append("   , sub.shop_id");
        query.append("   , SUM(sub.product_num) AS product_num");
        query.append(" FROM (");
        query.append("   SELECT");
        query.append("     TO_CHAR(t1.sales_date, 'YYYY/MM') AS sales_ym");
        if (super.getBean().isTaxFlag()) {
            query.append("     , t2.discount_detail_value_in_tax AS product_num");        
        } else {
            query.append("     , t2.discount_detail_value_no_tax AS product_num");
        }
        query.append("     , t2.detail_staff_id AS staff_id");
        query.append("     , t1.shop_id");
        query.append("   FROM");
        query.append("     data_sales AS t1");
        query.append("   INNER JOIN");
        query.append("     view_data_sales_detail_valid_with_total_discount AS t2");
        query.append("     ON (t1.shop_id = t2.shop_id AND t1.slip_no = t2.slip_no)");
        query.append("   WHERE");
        query.append("     t2.product_division IN (1, 2)");

        if (!targetShopList.isEmpty()) {
            query.append("   AND t1.shop_id IN (");
            for(Integer target : targetShopList) {
                query.append(SQLUtil.convertForSQL(target)).append(",");
            }
            query.deleteCharAt(query.length() - 1);
            query.append(")");
        }
        if (!targetStaffList.isEmpty()) {
            query.append("   AND t2.detail_staff_id IN (");
            for(Integer target : targetStaffList) {
                query.append(SQLUtil.convertForSQL(target)).append(",");
            }
            query.deleteCharAt(query.length() - 1);
            query.append(")");
        }

        if (super.getBean().getTargetDateFrom() != null) {
            query.append("   AND t1.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        }
        if (super.getBean().getTargetDateTo() != null) {
            query.append("   AND t1.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        }
        
        query.append(" ) sub");
        query.append(" GROUP BY");
        query.append("   sub.sales_ym");
        query.append("   , sub.staff_id");
        query.append("   , sub.shop_id");
        query.append(" ORDER BY");
        query.append("   sub.sales_ym");
        query.append("   , sub.shop_id");
        query.append("   , sub.staff_id");

        // SQL実行
        ResultSetWrapper rs = conn.executeQuery(query.toString());
        rs.beforeFirst();

        Map<Integer, Map<String, Long>> results = new LinkedHashMap<>();
        while (rs.next()) {
            Integer id;
            if(super.getBean().isShopFlag()) {
                id = rs.getInt("shop_id");
            } else if (super.getBean().isStaffFlag()) {
                id = rs.getInt("staff_id");
            } else {
                break;
            }
            if (!results.containsKey(id)) {
                results.put(id, new LinkedHashMap<String, Long>());
            }
            
            String salesYm = rs.getString("sales_ym");
            Long productNum = rs.getLong("product_num");
            if (results.get(id).containsKey(salesYm)) {
                productNum = productNum + results.get(id).get(salesYm);
            }
            results.get(id).put(salesYm, productNum);
        }
        return results;
    }
    
    /**
     * TO率を設定します。
     * 
     * @param conn DBコネクション
     * @param row 明細情報
     * @throws Exception 汎用例外
     */
    private void setToRateNum(
            Map<Integer, Map<String, Long>> toCountMap, OriginalReport01Bean row) throws Exception {
        Integer id;
        if (super.getBean().isShopFlag()) {
            id = row.getShopId();
        } else {
            id = row.getStaffId();
        }
        Map<String, Long> toCountMapYm = toCountMap.get(id);
        if(toCountMapYm == null) {
            return;
        }
        for(String salesYm : toCountMapYm.keySet()) {

            if (!row.getDetail().containsKey(salesYm)) {
                row.getDetail().put(salesYm, new OriginalReport01DetailBean());
            }

            OriginalReport01DetailBean bean = row.getDetail().get(salesYm);
            // TO率
            bean.setToRate(new OriginalListBean(null, toCountMapYm.get(salesYm)));
        }
    }
    
    /**
     * スタッフID・店舗IDをキーに年月とその時のTO数を持つマップを作成
     * 
     * @param conn DBコネクション
     * @param targetStaffList 検索対象スタッフIDのリスト
     * @param targetShopList 検索対象店舗IDのリスト
     * @return 人・店舗をキーに年月とその時のTO数を持つマップ
     * @throws Exception 汎用例外
     */
    private Map<Integer, Map<String, Long>> getToCount(
            ConnectionWrapper conn, List<Integer> targetShopList, List<Integer> targetStaffList) throws Exception {
        StringBuilder query = new StringBuilder();
        
        query.append(" SELECT ");
        query.append("   sub.sales_ym ");
        query.append("   , sub.shop_id ");
        query.append("   , sub.staff_id ");
        query.append("   , COUNT(*) as time_over_count ");
        query.append(" FROM ");
        query.append("   (  ");
        query.append("     SELECT ");
        query.append("       TO_CHAR(t1.sales_date, 'YYYY/MM') AS sales_ym ");
        query.append("       , t1.shop_id ");
        query.append("       , t1.slip_no ");
        query.append("       , t1.staff_id ");
        query.append("     FROM ");
        query.append("       data_sales AS t1  ");
        query.append("       INNER JOIN ( ");
        query.append("         SELECT ");
        query.append("           s1.shop_id ");
        query.append("           , s1.slip_no ");
        query.append("           , SUM( COALESCE(operation_time, 0) ) AS sum_operation_time ");
        query.append("         FROM ");
        query.append("           data_sales_detail AS s1  ");
        query.append("         LEFT OUTER JOIN mst_technic AS s2  ");
        query.append("           ON (s2.technic_id = s1.product_id AND s1.delete_date IS NULL AND s1.product_division IN (1, 3))  ");
        query.append("         GROUP BY s1.shop_id, s1.slip_no ");
        query.append("       ) AS t2 ");
        query.append("         ON (  ");
        query.append("           t1.shop_id = t2.shop_id  ");
        query.append("           AND t1.slip_no = t2.slip_no  ");
        query.append("         )  ");
        query.append("       INNER JOIN data_reservation AS t3  ");
        query.append("         ON (  ");
        query.append("           t1.shop_id = t3.shop_id  ");
        query.append("           AND t1.slip_no = t3.slip_no ");
        query.append("         )  ");
        query.append("     WHERE ");
        query.append("       t2.sum_operation_time < COALESCE(CEIL(EXTRACT(EPOCH FROM t3.leave_time - t3.start_time) / 60), 0) ");
        
        if (!targetShopList.isEmpty()) {
            query.append("   AND t1.shop_id IN (");
            for(Integer target : targetShopList) {
                query.append(SQLUtil.convertForSQL(target)).append(",");
            }
            query.deleteCharAt(query.length() - 1);
            query.append(")");
        }
        if (!targetStaffList.isEmpty()) {
            query.append("   AND t1.staff_id IN (");
            for(Integer target : targetStaffList) {
                query.append(SQLUtil.convertForSQL(target)).append(",");
            }
            query.deleteCharAt(query.length() - 1);
            query.append(")");
        }
        
        if (super.getBean().getTargetDateFrom() != null) {
            query.append("   AND t1.sales_date >= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateFrom()));
        }
        if (super.getBean().getTargetDateTo() != null) {
            query.append("   AND t1.sales_date <= ").append(
                    SQLUtil.convertForSQL(super.getBean().getTargetDateTo()));
        }
        
        query.append("   ) AS sub  ");
        query.append(" GROUP BY ");
        query.append("   sub.sales_ym  ");
        query.append("   , sub.shop_id  ");
        query.append("   , sub.staff_id  ");
        query.append(" ORDER BY ");
        query.append("   sub.sales_ym ");
        query.append("   , sub.shop_id  ");
        query.append("   , sub.staff_id  ");
        
        // SQL実行
        ResultSetWrapper rs = conn.executeQuery(query.toString());
        
        Map<Integer, Map<String, Long>> results = new LinkedHashMap<>();
        while (rs.next()) {
            Integer id;
            if(super.getBean().isShopFlag()) {
                id = rs.getInt("shop_id");
            } else {
                id = rs.getInt("staff_id");
            }
            if (!results.containsKey(id)) {
                results.put(id, new LinkedHashMap<String, Long>());
            }
            
            String salesYm = rs.getString("sales_ym");
            Long timeOverCount = rs.getLong("time_over_count");
            if (results.get(id).containsKey(salesYm)) {
                timeOverCount = timeOverCount + results.get(id).get(salesYm);
            }
            results.get(id).put(salesYm, timeOverCount);
        }
        return results;
    }
    
    private String getRrR2Sql(OriginalReport01Bean row, Date targetYmdFrom, Date targetYmdTo, Date periodDateFrom, Date periodDateTo) {
        //初期処理
        HashMap<String, String> paramMap = new HashMap<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        //再来算出期間
        paramMap.put("CalculationStartDate", format.format(periodDateFrom));
        paramMap.put("CalculationEndDate", format.format(periodDateTo));
        paramMap.put("TargetStartDate", format.format(targetYmdFrom));
        paramMap.put("TargetEndDate", format.format(targetYmdTo));
        
        //メインのFROM用SQL
        StringBuilder fromSql = new StringBuilder();
        fromSql.append("  ( ");
        fromSql.append("    select ");
        fromSql.append("        ds.slip_no");
        fromSql.append("      , ds.sales_date");
        fromSql.append("      , ds.customer_id");
        fromSql.append("      , ds.insert_date");
        fromSql.append("      , ( ");
        fromSql.append("        select");
        fromSql.append("          count(slip_no) + coalesce(max(before_visit_num), 0) ");
        fromSql.append("        from");
        fromSql.append("          data_sales ");
        fromSql.append("          inner join mst_customer ");
        fromSql.append("            using (customer_id) ");
        fromSql.append("        where");
        fromSql.append("          exists ( ");
        fromSql.append("            SELECT");
        fromSql.append("              1 ");
        fromSql.append("            FROM");
        fromSql.append("              data_sales_detail dsd ");
        fromSql.append("            WHERE");
        fromSql.append("              dsd.shop_id = data_sales.shop_id ");
        fromSql.append("              AND dsd.slip_no = data_sales.slip_no ");
        fromSql.append("              AND dsd.delete_date is null ");
        fromSql.append("              AND dsd.product_division in (1, 5, 6)");
        fromSql.append("          ) ");
        fromSql.append("          and data_sales.delete_date is null ");
        fromSql.append("          and customer_id = ds.customer_id ");
        fromSql.append("          and ( ");
        fromSql.append("            data_sales.sales_date < ds.sales_date ");
        fromSql.append("            or ( ");
        fromSql.append("              data_sales.sales_date = ds.sales_date ");
        fromSql.append("              and data_sales.insert_date <= ds.insert_date");
        fromSql.append("            )");
        fromSql.append("          )");
        fromSql.append("      ) as visit_num ");
        fromSql.append("    from");
        fromSql.append("      data_sales ds join mst_customer mc ");
        fromSql.append("        using (customer_id) ");
        fromSql.append("    where");
        fromSql.append("      exists ( ");
        fromSql.append("        select");
        fromSql.append("          1 ");
        fromSql.append("        from");
        fromSql.append("          data_sales_detail ");
        fromSql.append("        where");
        fromSql.append("          shop_id = ds.shop_id ");
        fromSql.append("          and slip_no = ds.slip_no ");
        fromSql.append("          and delete_date is null ");
        fromSql.append("          and product_division in (1, 5, 6)");
        fromSql.append("      ) ");
        fromSql.append("      and ");
        fromSql.append("      ds.delete_date is null ");
        fromSql.append("      and mc.customer_no <> '0' ");
        if (row.getShopId() != null) {
            if (this.getBean().isStaffFlag()) {
                //担当者選択時、店舗を条件にしない
            } else {
                fromSql.append("     and ds.shop_id in (").append(SQLUtil.convertForSQL(row.getShopId())).append(")");
            }
        }
        if (row.getStaffId() != null) {
            fromSql.append("     and ds.staff_id = ").append(SQLUtil.convertForSQL(row.getStaffId()));
        }
        //再来期間の日付をそのまま条件に使用
        fromSql.append("      and ds.sales_date between '").append(paramMap.get("CalculationStartDate")).append(" 00:00:00' and '")
                .append(paramMap.get("CalculationEndDate")).append(" 23:59:59'");
        fromSql.append("  ) t");

        ////////////////////////////////////////////////////////////////////////
        //部品SQL文
        //対象期間内に売上有無の判定
        ////////////////////////////////////////////////////////////////////////
        StringBuilder tmpSql = new StringBuilder();
        tmpSql.append(" select 1");
        tmpSql.append(" from");
        tmpSql.append("     (");
        tmpSql.append(" select");
        tmpSql.append("      ds.sales_date");
        tmpSql.append("     ,ds.customer_id");
        tmpSql.append("     ,ds.insert_date");
        tmpSql.append(" from");
        tmpSql.append("     data_sales ds");
        tmpSql.append(" join mst_customer mc");
        tmpSql.append("     using(customer_id)");
        tmpSql.append(" where");
        tmpSql.append("     exists");
        tmpSql.append(" (");
        tmpSql.append("     select 1");
        tmpSql.append("     from");
        tmpSql.append(" data_sales_detail");
        tmpSql.append("     where");
        tmpSql.append("     shop_id = ds.shop_id");
        tmpSql.append(" and slip_no = ds.slip_no");
        tmpSql.append(" and delete_date is null");
        tmpSql.append(" and product_division in (1,5,6)");
        tmpSql.append(" )");
        tmpSql.append("     and ds.delete_date is null");
        tmpSql.append("     and mc.customer_no <> '0'");
        tmpSql.append("     and ds.sales_date between '").append(paramMap.get("TargetStartDate")).append(" 00:00:00' and '")
                .append(paramMap.get("TargetEndDate")).append(" 23:59:59'");
        tmpSql.append("     ) w");
        tmpSql.append(" where");
        tmpSql.append(" w.customer_id = t.customer_id");
        tmpSql.append("     and (");
        tmpSql.append("     w.sales_date > t.sales_date");
        tmpSql.append("  or (w.sales_date = t.sales_date and w.insert_date > t.insert_date)");
        tmpSql.append(" )");

        // メインＳＱＬ
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT");
        sql.append("   COUNT(sub.all_new_reappearance) AS all_new_num,");
        sql.append("   COUNT(sub.all_fixed_reappearance) AS all_fixed_num,");
        sql.append("   COUNT(sub.r2_new_reappearance) AS r2_num,");
        sql.append("   COUNT(sub.rr_fixed_reappearance) AS rr_num");
        sql.append(" FROM");
        sql.append(" (");
        sql.append(" select");
        sql.append("     DISTINCT");
        sql.append("     t.sales_date,");
        sql.append("     t.customer_id,");
        sql.append("     t.insert_date,");
        sql.append("     TO_CHAR(t.sales_date, 'YYYY/MM') AS sales_ym,");
        sql.append("     case ");
        sql.append("     when t.visit_num = 1 ");
        sql.append("     then 1 ");
        sql.append("     else null");
        sql.append("     end");
        sql.append("     as all_new_reappearance,");
        sql.append("     case ");
        sql.append("     when t.visit_num >= 2 ");
        sql.append("     then 1 ");
        sql.append("     else null");
        sql.append("     end");
        sql.append("     as all_fixed_reappearance,");
        sql.append("     case ");
        sql.append("     when t.visit_num = 1 and exists ( ").append(tmpSql).append(") ");
        sql.append("     then 1 ");
        sql.append("     else null");
        sql.append("     end");
        sql.append("     as r2_new_reappearance,");
        sql.append("     case ");
        sql.append("     when t.visit_num >= 2 and exists ( ").append(tmpSql).append(") ");
        sql.append("     then 1 ");
        sql.append("     else NULL ");
        sql.append("     end ");
        sql.append("     as rr_fixed_reappearance ");
        sql.append(" from").append(fromSql);
        sql.append(") AS sub ");
        return sql.toString();
    }
}
