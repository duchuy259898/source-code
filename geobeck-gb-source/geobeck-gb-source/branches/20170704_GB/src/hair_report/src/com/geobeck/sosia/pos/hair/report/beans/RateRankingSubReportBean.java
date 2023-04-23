/*
 * RateRankingSubReportBean.java
 *
 * Created on 2008/09/22, 14:44
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
public class RateRankingSubReportBean
{
    
    private JRBeanCollectionDataSource subStaffRate;
    private JRBeanCollectionDataSource subStoreRate;
    
    /** Creates a new instance of RateRankingSubReportBean */
    public RateRankingSubReportBean()
    {
    }
    
    public JRBeanCollectionDataSource getSubStaffRate(){
        return this.subStaffRate;
    }
    
    public JRBeanCollectionDataSource getSubStoreRate(){
        return this.subStoreRate;
    }
    
    public void setSubStaffRate(JRBeanCollectionDataSource rank){
        this.subStaffRate = rank;
    }
    
    public void setSubStoreRate(JRBeanCollectionDataSource rank) {
        this.subStoreRate = rank;
    }
}
