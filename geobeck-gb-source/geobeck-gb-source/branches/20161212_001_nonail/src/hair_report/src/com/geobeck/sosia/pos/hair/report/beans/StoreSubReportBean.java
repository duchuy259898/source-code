/*
 * StoreSubReportBean.java
 *
 * Created on 2008/08/25, 16:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;


// include files for JRBeanCollection
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author gloridel
 * This class is used for setting/retrieving values of Technic Information.
 * Used in the Main Class StoreDailyReport
 */
public class StoreSubReportBean 
{
    // Technic Information Sub Report
    private JRBeanCollectionDataSource subStoreTechnicReport;
    private JRBeanCollectionDataSource subStoreTechcRateReport;
    private JRBeanCollectionDataSource subStoreExpenditureReport;
    private JRBeanCollectionDataSource subStaffAttendance;
    
    /** Creates a new instance of StoreSubReportBean */
    public StoreSubReportBean() 
    {
    }
    
    // Technic Information Sub Report Retrieve Function
    public JRBeanCollectionDataSource getSubStoreTechnicReport()
    {
        return this.subStoreTechnicReport;
    }
    
    //Technic Rate Information Sub Report Retrieve Function
    public JRBeanCollectionDataSource getSubStoreTechnicRateReport()
    {
        return this.subStoreTechcRateReport;
    }
    
    public JRBeanCollectionDataSource getSubStoreExpenditureReport()
    {
        return this.subStoreExpenditureReport;
    }
    
    public JRBeanCollectionDataSource getSubStaffAttendance()
    {
        return this.subStaffAttendance;
    }
    
    // Technic Information Sub Report Set Function
    public void setSubStoreTechnicReport(JRBeanCollectionDataSource subTechnic)
    {
        this.subStoreTechnicReport = subTechnic;
    }
    
    //Technic Rate Information Sub Report Set Function
    public void setSubStoreTechnicRateReport(JRBeanCollectionDataSource subTechnicRate)
    {
        this.subStoreTechcRateReport = subTechnicRate;
    }      
    
    public void setSubStoreExpenditureReport(JRBeanCollectionDataSource subExpend)
    {
        this.subStoreExpenditureReport = subExpend;
    }
    
    public void setSubStaffAttendance(JRBeanCollectionDataSource subAttend)
    {
        this.subStaffAttendance = subAttend;
    }
  
}
