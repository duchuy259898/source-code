/*
 * PersonalSubReportBean.java
 *
 * Created on 2008/08/27, 11:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.geobeck.sosia.pos.hair.report.beans;

// include files for JRBeanCollection
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author trino
 */
public class PersonalSubReportBean 
{
    private JRBeanCollectionDataSource subReportTechnic;
    private JRBeanCollectionDataSource subServiceReport;
    private JRBeanCollectionDataSource subStockReport;
    private JRBeanCollectionDataSource subNewServiceReport;
    
    /** Creates a new instance of PersonalSubReportBean */
    public PersonalSubReportBean() 
    {
    }
    
    public JRBeanCollectionDataSource getSubReportTechnic()
    {
        return this.subReportTechnic;
    }
    
    public JRBeanCollectionDataSource getSubServiceReport()
    {
        return this.subServiceReport;
    }
    
    public JRBeanCollectionDataSource getSubStockReport()
    {
        return this.subStockReport;
    }
    
    public JRBeanCollectionDataSource getSubNewServiceReport()
    {
        return this.subNewServiceReport;
    }
    
    public void setSubReportTechnic(JRBeanCollectionDataSource report)
    {
        this.subReportTechnic = report;
    }
    
    public void setSubServiceReport(JRBeanCollectionDataSource report)
    {
        this.subServiceReport = report;
    }
    
    public void setSubStockReport(JRBeanCollectionDataSource report)
    {
        this.subStockReport = report;
    }
    
    public void setSubNewServiceReport(JRBeanCollectionDataSource report)
    {
        this.subNewServiceReport = report;
    }
}
