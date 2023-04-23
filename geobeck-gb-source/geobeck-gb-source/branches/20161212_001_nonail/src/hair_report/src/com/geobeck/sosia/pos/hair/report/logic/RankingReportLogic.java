/*
 * RankingReportLogic.java
 *
 * Created on 2008/09/19, 18:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import com.geobeck.sosia.pos.hair.report.RankingReportPanelTom;
import com.geobeck.sosia.pos.util.MessageUtil;
import com.geobeck.swing.*;
import com.geobeck.util.NumberUtil;
import java.util.*;
import java.util.logging.Level;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

// use for JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;
import com.geobeck.sosia.pos.system.SystemInfo;
import com.geobeck.sql.*;

import com.geobeck.sosia.pos.hair.report.beans.SalesRankingSubReportBean;
import com.geobeck.sosia.pos.hair.report.beans.SalesRankingStaffBean;
import com.geobeck.sosia.pos.hair.report.beans.SalesRankingStoreBean;
import com.geobeck.sosia.pos.hair.report.beans.RateRankingSubReportBean;
import com.geobeck.sosia.pos.hair.report.beans.RateRankingStaffBean;
import com.geobeck.sosia.pos.hair.report.beans.RateRankingStoreBean;
import com.geobeck.sosia.pos.master.company.MstStaff;
import com.geobeck.util.SQLUtil;

/**
 * @author gloridel
 */
public class RankingReportLogic extends ReportGeneratorLogic {
    public static String LE60 = "��戵��60�l�ȉ���";
    public static String REPORT_SALESRANKING_JASPER = "SalesRanking.jasper";
    public static String REPORT_SALESRANKING_STAFF  = "SalesRanking_Staff.jasper";
    public static String REPORT_SALESRANKING_STORE  = "SalesRanking_Store.jasper";
    public static String REPORT_RATERANKING_JASPER  = "RateRanking.jasper";
    public static String REPORT_RATERANKING_STAFF   = "RateRanking_Staff.jasper";
    public static String REPORT_RATERANKING_STORE   = "RateRanking_Store.jasper";
    
    private ReportTypes reportType;
    private Date  outputDate = null;
    
    private ConnectionWrapper dbConnection = null;
    private Double totalPercent = null;
    private RankingReportPanelTom parentPanel;
    // </editor-fold>
    
    /** ���|�[�g��ʂ̗񋓌^ */
    public enum ReportTypes{
        
        Designate("�w�����W�v�\","RateRankingDesignate","�w��"),
        Gone("���q�䗦�W�v�\","RateRankingGone","���q"),
        Sales("�X�̔���W�v�\","SalesRanking"),
        TechnicSales("�Z�p����W�v�\","TechnicSalesRanking");
        
        private String str;
        private String label;
        private String outputFileName;
        
        private ReportTypes(String str, String outputFileName){
            this.str = str;
            this.outputFileName = outputFileName;
        }
        
        private ReportTypes(String str, String outputFileName, String label){
            this.str = str;
            this.outputFileName = outputFileName;
            this.label = label;
        }
        
        @Override
        public String toString() {return str;}
        
        /** �o�̓t�@�C�������擾���� */
        public String getOutputFileName() {return outputFileName;}
        /** ���x�����擾���� */
        public String getLabel() {return label;}
    }
    
    /** �R���X�g���N�^
     * @param reportType ���|�[�g�̎��
     * @param date �W�v����
     */
    public RankingReportLogic(ReportTypes reportType, Date date, RankingReportPanelTom panel) {
        this.reportType = reportType;
        outputDate = date;
        this.parentPanel = panel;
        dbConnection = SystemInfo.getConnection();
    }
    
    /** ���|�[�g���쐬���ĉ{������ */
    public void viewRankingReport(int fileType){
        
        HashMap<String,Object> paramMap = new HashMap<String,Object>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'�N' MM'���x'");
        
        paramMap.put("RankingType", reportType.toString());
        paramMap.put("OutputDate", dateFormat.format(outputDate));
        
        switch (reportType){
            case Sales:
            case TechnicSales:
                
                ArrayList<SalesRankingSubReportBean> salesRankingBeans = getSalesRankingBeans();
                
                if(salesRankingBeans == null){
                    //�o�͑Ώۃf�[�^������܂���B
                    MessageDialog.showMessageDialog(parentPanel,
                            MessageUtil.getMessage(4001),
                            parentPanel.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                paramMap.put("salesPerPersonTotal",totalPercent);
                generateFile(salesRankingBeans, paramMap, fileType);
                break;
                
            case Designate:
            case Gone:
                
                ArrayList<RateRankingSubReportBean> rateRankingBeans = getRateRankingBeans();
                
                if(rateRankingBeans == null){
                    //�o�͑Ώۃf�[�^������܂���B
                    MessageDialog.showMessageDialog(parentPanel,
                            MessageUtil.getMessage(4001),
                            parentPanel.getTitle(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                generateFile(rateRankingBeans, paramMap, fileType);
                break;
        }
    }
    
    /** �w�����܂��͎��q�䗦�W�v�\ */
    private ArrayList<RateRankingSubReportBean> getRateRankingBeans(){
        ArrayList<RateRankingSubReportBean> beanList =
                new ArrayList<RateRankingSubReportBean>();
        
        RateRankingSubReportBean bean = new RateRankingSubReportBean();
        ArrayList<RateRankingStaffBean> staffRateRanking = getStaffRateRanking();
        ArrayList<RateRankingStoreBean> storeRateRanking = getStoreRateRanking();
        if(staffRateRanking.size() == 0 && storeRateRanking.size() == 0){
            return null;
        }
        bean.setSubStaffRate(new JRBeanCollectionDataSource(staffRateRanking));
        bean.setSubStoreRate(new JRBeanCollectionDataSource(storeRateRanking));
        
        beanList.add(bean);
        return beanList;
    }
    
    /** �X�ܔ���܂��͋Z�p����W�v�\ */
    private ArrayList<SalesRankingSubReportBean> getSalesRankingBeans() {

        // �X�^�b�t�ʏW�v
        ArrayList<SalesRankingStaffBean> staffSalesRanking = getSalesRanking_Staff();
        // �X�ܕʏW�v
        ArrayList<SalesRankingStoreBean> storeSalesRanking = getSalesRanking_Store();
        
        if(staffSalesRanking.size() == 0 && storeSalesRanking.size() == 0) return null;
        
        SalesRankingSubReportBean bean = new SalesRankingSubReportBean();
        bean.setSubStaffRank(new JRBeanCollectionDataSource(staffSalesRanking));
        bean.setSubStoreRank(new JRBeanCollectionDataSource(storeSalesRanking));
        
        ArrayList<SalesRankingSubReportBean> beanList = new ArrayList<SalesRankingSubReportBean>();
        beanList.add(bean);

        return beanList;
    }
    
    /** PDF��Excel�t�@�C�����쐬 */
    private boolean generateFile(
            Collection collection,
            HashMap<String,Object> paramMap,
            int fileType) {
        boolean isCreated  = false;
        
        try {
            String inputFileName = null;
            
            switch(reportType ) {
                case Sales:
                case TechnicSales:
                    paramMap.put("StaffRank", loadReport(
                            REPORT_SALESRANKING_STAFF,
                            REPORT_FILE_TYPE_JASPER));
                    paramMap.put("StoreRank", loadReport(
                            REPORT_SALESRANKING_STORE,
                            REPORT_FILE_TYPE_JASPER));
                    inputFileName = REPORT_SALESRANKING_JASPER;
                    break;
                case Designate:
                case Gone:
                    paramMap.put("StaffRate", loadReport(
                            REPORT_RATERANKING_STAFF,
                            REPORT_FILE_TYPE_JASPER));
                    paramMap.put("StoreRate", loadReport(
                            REPORT_RATERANKING_STORE,
                            REPORT_FILE_TYPE_JASPER));
                    paramMap.put("RankingTypeLabel", reportType.getLabel());
                    inputFileName = REPORT_RATERANKING_JASPER;
                    break;
                default:
                    return false;
            }
            
            JasperPrint print = JasperFillManager.fillReport(
                    loadReport(inputFileName, REPORT_FILE_TYPE_JASPER),
                    paramMap,
                    new JRBeanCollectionDataSource(collection));
            
            switch(fileType) {
                case EXPORT_FILE_PDF:
                    isCreated = generateAndPreviewPDFFile(
                            reportType.getOutputFileName(), print);
                    break;
                case EXPORT_FILE_XLS:
                    isCreated = generateAndPreviewXLSFile(
                            reportType.getOutputFileName(), print);
                    break;
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return isCreated;
    }
    
    /** �X�^�b�t�̏��ʁF�Z�p����W�v�\�A�X�̔���W�v�\ */
    private ArrayList getSalesRanking_Staff() {
        
        ArrayList<SalesRankingStaffBean> beanList = new ArrayList<SalesRankingStaffBean>();
        SalesRankingStaffBean bean = null;
        
        try {
            
            String sql = "";
            
            if (reportType == ReportTypes.TechnicSales) {
                sql = getTechSalesRanking_StaffSQL();
            } else {
                sql = getShopSalesRanking_StaffSQL();
            }
            
            ResultSetWrapper rs = dbConnection.executeQuery(sql);

            // ����
            long sales = 0;
            // ����O�l
            long previusValue = Long.MAX_VALUE;

            int rankCounter = 1;
            
            while (rs.next()) {
            
                bean = new SalesRankingStaffBean(rs.getString("staff_name"),rs.getString("shop_name"));
                sales = rs.getLong("sales");
                
                if (!rs.wasNull()) {
                    bean.setSales(sales);
                    bean.setRank(rankCounter);
                    previusValue = sales;
                }
                
                System.out.println(bean.getStaffName());
                
                beanList.add(bean);
                
                rankCounter++;
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return beanList;
    }
    
    /** �X�܂̏��ʁF�Z�p����W�v�\�A�X�̔���W�v�\ */
    private ArrayList getSalesRanking_Store() {
        
        ArrayList<SalesRankingStoreBean> beanList = new ArrayList<SalesRankingStoreBean>();
        SalesRankingStoreBean bean = null;
        
        /** ���� */
        long sales;
        /** ���オnull�łȂ� */
        boolean salesWasNotNull;
        /** ����O�l */
        long previusValue = Long.MAX_VALUE;
        /** ���㍇�v */
        long sumSales = 0L;
        /** �X�^�b�t�� */
        long staffCount;
        /** ���X�^�b�t�� */
        Long totalStaffCount = 0L;
        /** �N�G�� */
        
        try {
            
            String sql = "";
            
            if (reportType == ReportTypes.TechnicSales) {
                sql = getTechSalesRanking_StoreSQL();
            } else {
                sql = getShopSalesRanking_StoreSQL();
            }
            
            ResultSetWrapper rs = dbConnection.executeQuery(sql);
            
            int rankCounter = 1;
            
            while (rs.next()) {
                bean = new SalesRankingStoreBean(rs.getString("shop_name"));
                
                sales = rs.getLong("sales");
                
                if (salesWasNotNull = !rs.wasNull()) {
                    bean.setSales(sales);
                    sumSales += sales;
                    bean.setRank(rankCounter);
                    previusValue = sales;
                }
                
                rs.getInt("shop_id");
                
                if (rs.wasNull()) {
                    // �s���ȓX�܂�����Ƃ��̓X�^�b�t���̏W�v����߂�
                    totalStaffCount = null; 
                    
                }else{
                    
                    staffCount = rs.getLong("staff_count");
                    if (staffCount != 0 && salesWasNotNull) {
                        bean.setSalesPerPerson(NumberUtil.round((double)sales / (double)staffCount));
                    }
                    
                    if (!rs.wasNull()) {
                        bean.setStaffCount(staffCount);
                        if (totalStaffCount != null) {
                            totalStaffCount += staffCount;
                        }
                    }
                }
                beanList.add(bean);
                
                rankCounter++;
            }
            
            if (totalStaffCount != null && totalStaffCount != 0) {
                totalPercent = (double)sumSales / (double)totalStaffCount;
            }
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return beanList;
    }
    
    /** �X�^�b�t�̏��ʁF�w�����W�v�\�A���q�䗦�W�v�\ */
    private ArrayList getStaffRateRanking() {
        ArrayList<RateRankingStaffBean> beanList = new ArrayList<RateRankingStaffBean>();
        RateRankingStaffBean bean = null;
        
        /** ��S�� */
        int slipCount;
        /** �w���܂��͎��q�� */
        int count;
        /** �ЂƂO�̃X�^�b�t���戵�U�O������������ */
        boolean wasGT60 = true;
        /** �p�[�Z���g */
        double percent;
        /** �p�[�Z���g�O�l */
        double previusPercent = Double.MAX_VALUE;
        /** �N�G�� */
        String sql;
        
        switch (reportType){
            case Designate:
                sql = RateRankingDesignate_StaffSQL();
                break;
            case Gone:
                sql = RateRankingGone_StaffSQL();
                break;
            default:
                return beanList;
        }

        try {
            ResultSetWrapper rs = dbConnection.executeQuery(sql);
            
            int rankCounter = 1;
            
            while (rs.next()) {
                bean = new RateRankingStaffBean(
                        rs.getString("staff_name"),rs.getString("shop_name"));
                slipCount = rs.getInt("slip_count");
                if(!rs.wasNull()){
                    bean.setInChargeNo(slipCount);
                }
                count = rs.getInt("s_count");
                if(!rs.wasNull()){
                    bean.setCount(count);
                }
                percent = rs.getDouble("percent");
                if(!rs.wasNull()){
                    bean.setPercent(percent);
                }
                if(wasGT60){
                    if(slipCount > 60){
                        bean.setStaffIndex(rankCounter);
                        previusPercent = rankCounter;
                    }else{
                        wasGT60 = false;
                        beanList.add(new RateRankingStaffBean((String)null));
                        beanList.add(new RateRankingStaffBean(LE60));
                    }
                }
                beanList.add(bean);
                
                rankCounter++;
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return beanList;
    }
    
    /** �X�܂̏��ʁF�w�����W�v�\�A���q�䗦�W�v�\ */
    private ArrayList getStoreRateRanking() {
        ArrayList<RateRankingStoreBean> beanList = new ArrayList<RateRankingStoreBean>();
        RateRankingStoreBean bean = null;
        /** ��S�� */
        int slipCount;
        /** �w���܂��͎��q�� */
        int count;
        /** �p�[�Z���g */
        double percent;
        /** �p�[�Z���g�O�l */
        double previusPercent = Double.MAX_VALUE;
        /** �N�G�� */
        String sql;
        
        switch (reportType){
            case Designate:
                sql = RateRankingDesignate_StoreSQL();
                break;
            case Gone:
                sql = RateRankingGone_StoreSQL();
                break;
            default:
                return beanList;
        }

        try {
            ResultSetWrapper rs = dbConnection.executeQuery(sql);
            
            int rankCounter = 1;
            
            while (rs.next()) {
                bean = new RateRankingStoreBean(rs.getString("shop_name"));
                
                slipCount = rs.getInt("slip_count");
                if(!rs.wasNull()){
                    bean.setInChargeNo(slipCount);
                }
                
                count = rs.getInt("s_count");
                if(!rs.wasNull()){
                    bean.setCount(count);
                }
                
                percent = rs.getDouble("percent");
                if(!rs.wasNull()){
                    bean.setPercent(percent);
                    bean.setStoreIndex(rankCounter);
                    previusPercent = percent;
                }
                beanList.add(bean);
                
                rankCounter++;
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return beanList;
    }
    
    /** �w�������ʁi�X�^�b�t�j�̃N�G���[ */
    private String RateRankingDesignate_StaffSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      stf.staff_name1 || '�@' || stf.staff_name2 as staff_name");
        sql.append("     ,coalesce(t.slip_count, 0) as slip_count");
        sql.append("     ,coalesce(t.s_count, 0) as s_count");
        sql.append("     ,case when coalesce(t.slip_count, 0) > 0");
        sql.append("         then coalesce(t.s_count, 0)::numeric / coalesce(t.slip_count, 0)");
        sql.append("         else 0");
        sql.append("      end as percent");
        sql.append("     ,ms.shop_name");
        sql.append(" from");
        sql.append("     mst_staff stf");
        sql.append("         left join data_shop_staff dss");
        sql.append("                on dss.staff_id = stf.staff_id");
        sql.append("               and date_trunc('month', dss.yearmonth) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("               and dss.delete_date is null");
        sql.append("         left join mst_shop ms");
        sql.append("                on ms.shop_id = dss.shop_id");
        sql.append("         left join ");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      staff_id");
        sql.append("                     ,count(*) as slip_count");
        sql.append("                     ,sum(case when designated_flag then 1 else 0 end) as s_count");
        sql.append("                 from");
        sql.append("                     view_data_sales_valid ds");
        sql.append("                 where");
        sql.append("                         date_trunc('month', sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("                     and exists");
        sql.append("                         (");
        sql.append("                             select 1");
        sql.append("                             from");
        sql.append("                                 view_data_sales_detail_valid dsd");
        sql.append("                             where");
        sql.append("                                     dsd.shop_id = ds.shop_id");
        sql.append("                                 and dsd.slip_no = ds.slip_no");
        sql.append("                                 and dsd.product_division = 1");
        sql.append("                         )");
        sql.append("                     and " + getNotExistsMonitorSQL());
        sql.append("                 group by");
        sql.append("                     staff_id");
        sql.append("             ) t");
        sql.append("             on stf.staff_id = t.staff_id");
        sql.append(" where");
        sql.append("     stf.delete_date is null");
        sql.append(" order by");
        sql.append("      case when slip_count > 60 then 1 else 0 end desc");
        sql.append("     ,percent desc");
        sql.append("     ,slip_count desc");

        return sql.toString();
    }
    
    /** �w�������ʁi�X�܁j�̃N�G���[ */
    private String RateRankingDesignate_StoreSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      ms.shop_id");
        sql.append("     ,max(ms.shop_name) as shop_name");
        sql.append("     ,count(distinct ds.slip_no) as slip_count");
        sql.append("     ,sum(case when designated_flag then 1 else 0 end) as s_count");
        sql.append("     ,case when count(distinct ds.slip_no) > 0");
        sql.append("         then sum(case when designated_flag then 1 else 0 end)::numeric / count(distinct ds.slip_no)");
        sql.append("         else 0");
        sql.append("      end as percent");
        sql.append(" from");
        sql.append("     view_data_sales_valid ds");
        sql.append("         inner join mst_shop ms");
        sql.append("                on ms.shop_id = ds.shop_id");
        sql.append("               and ms.delete_date is null");
        sql.append(" where");
        sql.append("         date_trunc('month', sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("     and exists");
        sql.append("         (");
        sql.append("             select 1");
        sql.append("             from");
        sql.append("                 view_data_sales_detail_valid dsd");
        sql.append("             where");
        sql.append("                     dsd.shop_id = ds.shop_id");
        sql.append("                 and dsd.slip_no = ds.slip_no");
        sql.append("                 and dsd.product_division = 1");
        sql.append("         )");
        sql.append("     and " + getNotExistsMonitorSQL());
        sql.append(" group by");
        sql.append("     ms.shop_id");
        sql.append(" order by");
        sql.append("      percent desc");
        sql.append("     ,slip_count desc");

        return sql.toString();

    }
    
    /** ���q�����ʁi�X�^�b�t�j�̃N�G���[ */
    private String RateRankingGone_StaffSQL() {

        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      stf.staff_name1 || '�@' || stf.staff_name2 as staff_name");
        sql.append("     ,coalesce(t.slip_count, 0) as slip_count");
        sql.append("     ,coalesce(t.s_count, 0) as s_count");
        sql.append("     ,case when coalesce(t.slip_count, 0) > 0");
        sql.append("         then coalesce(t.s_count, 0)::numeric / coalesce(t.slip_count, 0)");
        sql.append("         else 0");
        sql.append("      end as percent");
        sql.append("     ,ms.shop_name");
        sql.append(" from");
        sql.append("     mst_staff stf");
        sql.append("         left join data_shop_staff dss");
        sql.append("                on dss.staff_id = stf.staff_id");
        sql.append("               and date_trunc('month', dss.yearmonth) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("               and dss.delete_date is null");
        sql.append("         left join mst_shop ms");
        sql.append("                on ms.shop_id = dss.shop_id");
        sql.append("         left join ");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      staff_id");
        sql.append("                     ,count(*) as slip_count");
        sql.append("                     ,count(distinct");
        sql.append("                         case");
        sql.append("                             when mc.customer_no <> '0'");
        sql.append("                              and not exists");
        sql.append("                                 (");
        sql.append("                                     select 1 from data_sales");
        sql.append("                                     where");
        sql.append("                                             delete_date is null");
        sql.append("                                         and shop_id = ds.shop_id");
        sql.append("                                         and customer_id = ds.customer_id");
        sql.append("                                         and sales_date > ds.sales_date");
        sql.append("                                         and sales_date < date_trunc('month', ds.sales_date) + interval '4 month'");
        sql.append("                                 ) then ds.slip_no end");
        sql.append("                      ) as s_count");
        sql.append("                 from");
        sql.append("                     view_data_sales_valid ds");
        sql.append("                         join mst_customer mc");
        sql.append("                             using(customer_id)");
        sql.append("                 where");
        sql.append("                         date_trunc('month', sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("                     and exists");
        sql.append("                         (");
        sql.append("                             select 1");
        sql.append("                             from");
        sql.append("                                 view_data_sales_detail_valid dsd");
        sql.append("                             where");
        sql.append("                                     dsd.shop_id = ds.shop_id");
        sql.append("                                 and dsd.slip_no = ds.slip_no");
        sql.append("                                 and dsd.product_division = 1");
        sql.append("                         )");
        sql.append("                     and " + getNotExistsMonitorSQL());
        sql.append("                 group by");
        sql.append("                     staff_id");
        sql.append("             ) t");
        sql.append("             on stf.staff_id = t.staff_id");
        sql.append(" where");
        sql.append("     stf.delete_date is null");
        sql.append(" order by");
        sql.append("      case when slip_count > 60 then 1 else 0 end desc");
        sql.append("     ,percent");
        sql.append("     ,slip_count desc");

        return sql.toString();
    }
    
    /** ���q�����ʁi�X�܁j�̃N�G���[ */
    private String RateRankingGone_StoreSQL() {

        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("     t.*");
        sql.append("     ,case when t.slip_count > 0");
        sql.append("         then t.s_count::numeric / t.slip_count");
        sql.append("         else 0");
        sql.append("      end as percent");
        sql.append(" from");
        sql.append("     (");
        sql.append("         select");
        sql.append("              ms.shop_id");
        sql.append("             ,max(ms.shop_name) as shop_name");
        sql.append("             ,count(distinct ds.slip_no) as slip_count");
        sql.append("             ,count(distinct");
        sql.append("                 case");
        sql.append("                     when mc.customer_no <> '0'");
        sql.append("                      and not exists");
        sql.append("                         (");
        sql.append("                             select 1 from data_sales");
        sql.append("                             where");
        sql.append("                                     delete_date is null");
        sql.append("                                 and shop_id = ds.shop_id");
        sql.append("                                 and customer_id = ds.customer_id");
        sql.append("                                 and sales_date > ds.sales_date");
        sql.append("                                 and sales_date < date_trunc('month', ds.sales_date) + interval '4 month'");
        sql.append("                         ) then ds.slip_no end");
        sql.append("              ) as s_count");
        sql.append("         from");
        sql.append("             view_data_sales_valid ds");
        sql.append("                 join mst_customer mc");
        sql.append("                     using(customer_id)");
        sql.append("                 inner join mst_shop ms");
        sql.append("                        on ms.shop_id = ds.shop_id");
        sql.append("                       and ms.delete_date is null");
        sql.append("         where");
        sql.append("                 date_trunc('month', sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("             and exists");
        sql.append("                 (");
        sql.append("                     select 1");
        sql.append("                     from");
        sql.append("                         view_data_sales_detail_valid dsd");
        sql.append("                     where");
        sql.append("                             dsd.shop_id = ds.shop_id");
        sql.append("                         and dsd.slip_no = ds.slip_no");
        sql.append("                         and dsd.product_division = 1");
        sql.append("                 )");
        sql.append("             and " + getNotExistsMonitorSQL());
        sql.append("         group by");
        sql.append("             ms.shop_id");
        sql.append("     ) t");
        sql.append(" order by");
        sql.append("      percent");
        sql.append("     ,slip_count desc");

        return sql.toString();

    }
    
    /** �Z�p���㏇�ʁi�X�^�b�t�j�̃N�G���[ */
    private String getTechSalesRanking_StaffSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      stf.staff_name1 || '�@' || stf.staff_name2 as staff_name");
        sql.append("     ,coalesce(t.sales, 0) as sales");
        sql.append("     ,ms.shop_name");
        sql.append(" from");
        sql.append("     mst_staff stf");
        sql.append("         left join data_shop_staff dss");
        sql.append("                on dss.staff_id = stf.staff_id");
        sql.append("               and date_trunc('month', dss.yearmonth) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("               and dss.delete_date is null");
        sql.append("         left join mst_shop ms");
        sql.append("                on ms.shop_id = dss.shop_id");
        sql.append("         left join ");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      staff_id");
        sql.append("                     ,sum(discount_detail_value_no_tax) as sales");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                         date_trunc('month', sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("                     and product_division = 1");
        sql.append("                 group by");
        sql.append("                     staff_id");
        sql.append("             ) t");
        sql.append("             on stf.staff_id = t.staff_id");
        sql.append(" where");
        sql.append("     stf.delete_date is null");
        sql.append(" order by");
        sql.append("     sales desc");

        return sql.toString();
    }
    
    /** �Z�p���㏇�ʁi�X�܁j�̃N�G���[ */
    private String getTechSalesRanking_StoreSQL() {

        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      ms.shop_id");
        sql.append("     ,max(ms.shop_name) as shop_name");
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             count(distinct staff_id)");
        sql.append("         from");
        sql.append("             data_shop_staff dss");
        sql.append("         where");
        sql.append("                 dss.shop_id = ms.shop_id");
        sql.append("             and date_trunc('month', dss.yearmonth) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("             and dss.delete_date is null");
        sql.append("      ) as staff_count");
        sql.append("     ,sum(ds.discount_detail_value_no_tax) as sales");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid ds");
        sql.append("         inner join mst_shop ms");
        sql.append("                on ms.shop_id = ds.shop_id");
        sql.append("               and ms.delete_date is null");
        sql.append(" where");
        sql.append("         date_trunc('month', ds.sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("     and ds.product_division = 1");
        sql.append(" group by");
        sql.append("     ms.shop_id");
        sql.append(" order by");
        sql.append("     sales desc");

        return sql.toString();
    }
   
    /** �X�̔��㏇�ʁi�X�^�b�t�j�̃N�G���[ */
    private String getShopSalesRanking_StaffSQL() {
        
        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      stf.staff_name1 || '�@' || stf.staff_name2 as staff_name");
        sql.append("     ,coalesce(t.sales, 0) as sales");
        sql.append("     ,ms.shop_name");
        sql.append(" from");
        sql.append("     mst_staff stf");
        sql.append("         left join data_shop_staff dss");
        sql.append("                on dss.staff_id = stf.staff_id");
        sql.append("               and date_trunc('month', dss.yearmonth) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("               and dss.delete_date is null");
        sql.append("         left join mst_shop ms");
        sql.append("                on ms.shop_id = dss.shop_id");
        sql.append("         left join ");
        sql.append("             (");
        sql.append("                 select");
        sql.append("                      detail_staff_id as staff_id");
        sql.append("                     ,sum(discount_detail_value_no_tax) as sales");
        sql.append("                 from");
        sql.append("                     view_data_sales_detail_valid");
        sql.append("                 where");
        sql.append("                         date_trunc('month', sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("                     and product_division in (2, 4)");
        sql.append("                     and product_id in");
        sql.append("                         (");
        sql.append("                             select");
        sql.append("                                 a.item_id");
        sql.append("                             from");
        sql.append("                                 mst_item a");
        sql.append("                                     join mst_item_class b");
        sql.append("                                     using(item_class_id)");
        sql.append("                             where");
        sql.append("                                 b.item_class_contracted_name <> 'OTH2'");
        sql.append("                         )");
        sql.append("                 group by");
        sql.append("                     detail_staff_id");
        sql.append("             ) t");
        sql.append("             on stf.staff_id = t.staff_id");
        sql.append(" where");
        sql.append("     stf.delete_date is null");
        sql.append(" order by");
        sql.append("     sales desc");
        
        return sql.toString();
        
    }
    
    /** �X�̔��㏇�ʁi�X�܁j�̃N�G���[ */
    private String getShopSalesRanking_StoreSQL() {

        StringBuilder sql = new StringBuilder(1000);

        sql.append(" select");
        sql.append("      ms.shop_id");
        sql.append("     ,max(ms.shop_name) as shop_name");
        sql.append("     ,(");
        sql.append("         select");
        sql.append("             count(distinct staff_id)");
        sql.append("         from");
        sql.append("             data_shop_staff dss");
        sql.append("         where");
        sql.append("                 dss.shop_id = ms.shop_id");
        sql.append("             and date_trunc('month', dss.yearmonth) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("             and dss.delete_date is null");
        sql.append("      ) as staff_count");
        sql.append("     ,sum(ds.discount_detail_value_no_tax) as sales");
        sql.append(" from");
        sql.append("     view_data_sales_detail_valid ds");
        sql.append("         inner join mst_shop ms");
        sql.append("                on ms.shop_id = ds.shop_id");
        sql.append("               and ms.delete_date is null");
        sql.append(" where");
        sql.append("         date_trunc('month', ds.sales_date) = " + SQLUtil.convertForSQL(outputDate));
        sql.append("     and ds.product_division in (2, 4)");
        sql.append("     and ds.product_id in");
        sql.append("         (");
        sql.append("             select");
        sql.append("                 a.item_id");
        sql.append("             from");
        sql.append("                 mst_item a");
        sql.append("                     join mst_item_class b");
        sql.append("                     using(item_class_id)");
        sql.append("             where");
        sql.append("                 b.item_class_contracted_name <> 'OTH2'");
        sql.append("         )");
        sql.append(" group by");
        sql.append("     ms.shop_id");
        sql.append(" order by");
        sql.append("     sales desc");

        return sql.toString();
    }
    
    private String getShopName(int shopId ) {
        
        String sql= "select mst_shop.shop_name from mst_shop " +
                "where mst_shop.shop_id = " + shopId +
                " and mst_shop.delete_date is null ";
        String storename = null;
        
        try {
            ResultSetWrapper rs = dbConnection.executeQuery(sql);
            if( rs.next()) {
                storename = rs.getString("shop_name");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return storename;
    }
    
    /** �X�^�b�t���X�g���擾���� */
    private void  getstaffList() {

        String sql =  " select * from mst_staff  where delete_date is null" ;
        MstStaff staff = null;
        try {
            ResultSetWrapper rs = dbConnection.executeQuery(sql);
            while( rs.next() ) {
                staff = new MstStaff();
            }
        } catch(Exception e){} // Todo:: Exception handling
    }
    
    private static String getNotExistsMonitorSQL() {
        
        StringBuilder sql = new StringBuilder(1000);
        sql.append(" not exists");
        sql.append(" (");
        sql.append("     select 1");
        sql.append("     from");
        sql.append("         view_data_sales_detail_valid dsd");
        sql.append("             left join mst_technic mt");
        sql.append("                    on dsd.product_id = mt.technic_id");
        sql.append("             left join mst_technic_class mtc");
        sql.append("                    on mt.technic_class_id = mtc.technic_class_id");
        sql.append("     where");
        sql.append("             dsd.shop_id = ds.shop_id");
        sql.append("         and dsd.slip_no = ds.slip_no");
        sql.append("         and dsd.product_division = 1");
        sql.append("         and mtc.technic_class_name = '���j�^'");
        sql.append(" )");
        
        return sql.toString();
    }
}
