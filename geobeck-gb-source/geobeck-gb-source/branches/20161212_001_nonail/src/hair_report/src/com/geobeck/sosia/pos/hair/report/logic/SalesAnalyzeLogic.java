/*
 * SalesAnalyzeLogic.java
 *
 * Created on 2008/10/08, 16:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.logic;

import java.util.*;
import java.util.logging.Level;

// use for JasperReports
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.*;

import com.geobeck.sosia.pos.system.SystemInfo;

import com.geobeck.sosia.pos.master.company.MstStaff;
import org.jfree.data.Range;

/**
 * ���㕪��PDF�쐬���W�b�N
 * @author trino
 */
public class SalesAnalyzeLogic extends ReportGeneratorLogic {
    
    /** �R���X�g���N�^ */
    public SalesAnalyzeLogic() {}
    
    public static String REPORT_JASPER = "SalesAnalyze.jasper";
    public static String REPORT = "SalesAnalyze";
    
    private ArrayList<MstStaff> StaffList =  null;
    
    /** �o�̓t�@�C�����쐬
     * @param�@collection �f�[�^
     * @param�@paramMap �p�����[�^�̃}�b�v
     * @param�@exportType �o�͂���t�@�C���^�C�v(EXPORT_FILE_PDF �܂��� EXPORT_FILE_XLS)
     */
    public void generateFile(
            Collection beansSelectedYear,
            HashMap<String,Object> paramMap,
            Range r,
            int exportType) {
        try {
            JasperReport reportMain = null;
            reportMain = this.loadReport(REPORT_JASPER, REPORT_FILE_TYPE_JASPER);
            JasperPrint print = JasperFillManager.fillReport(
                    reportMain,
                    paramMap,
                    new JRBeanCollectionDataSource(beansSelectedYear));
            switch (exportType){
                case EXPORT_FILE_PDF:
                    this.generateAndPreviewPDFFile(REPORT, print);
                    break;
                case EXPORT_FILE_XLS:
                    this.generateAndPreviewXLSFile(REPORT, print);
                    break;
            }
            
        } catch(Exception e) {
            SystemInfo.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return;
    }// ���\�b�h�I�[
}